<!DOCTYPE html>
<meta charset="utf-8">
<style>

//CSS for changing visual style
svg {
  font: 12px sans-serif;
}

.backgroundDetail path {
  fill: none;
  stroke: #ddd;
  shape-rendering: crispEdges;
}

.foregroundDetail path {
  fill: none;
  stroke: #00b300;
}

.brush .extent {
  fill-opacity: .3;
  stroke: #fff;
  shape-rendering: crispEdges;
}

.axis line,
.axis path {
  fill: none;
  stroke: #000;
  shape-rendering: crispEdges;
}

.axis text {
  text-shadow: 0 1px 0 #fff, 1px 0 0 #fff, 0 -1px 0 #fff, -1px 0 0 #fff;
  cursor: move;
}

</style>
<body>
<script src="//d3js.org/d3.v3.min.js"></script>
<script>

var margin = {top: 40, right: 10, bottom: 10, left: 10},
    width = 1020 - margin.left - margin.right,
    height = 600 - margin.top - margin.bottom;

var x = d3.scale.ordinal().rangePoints([0, width], 1),
    y = {},
    dragging = {};

var line = d3.svg.line(),
    axis = d3.svg.axis().orient("left"),
    backgroundDetail,
    foregroundDetail;

var svg = d3.select("body").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

d3.csv("csvraw", function(error, measurements) {

  // Extract the list of dimensions and create a scale for each.
  x.domain(dimensions = d3.keys(measurements[0]).filter(function(d) {
    return d != "name" && (y[d] = d3.scale.linear()
        .domain(d3.extent(measurements, function(p) { return +p[d]; }))
        .range([height, 0]));
  }));

  // Adds grey backgroundDetail to make info clearer.
  backgroundDetail = svg.append("g")
      .attr("class", "backgroundDetail")
    .selectAll("path")
      .data(measurements)
    .enter().append("path")
      .attr("d", path);

  // Adds foregroundDetail lines to make info clearer.
  foregroundDetail = svg.append("g")
      .attr("class", "foregroundDetail")
    .selectAll("path")
      .data(measurements)
    .enter().append("path")
      .attr("d", path);

  // Creates a group element for each dimension in the input file.
  var g = svg.selectAll(".dimension")
      .data(dimensions)
    .enter().append("g")
      .attr("class", "dimension")
      .attr("transform", function(d) { return "translate(" + x(d) + ")"; })
      .call(d3.behavior.drag()
        .origin(function(d) { return {x: x(d)}; })
        .on("dragstart", function(d) {
          dragging[d] = x(d);
          backgroundDetail.attr("visibility", "hidden");
        })
        .on("drag", function(d) {
          dragging[d] = Math.min(width, Math.max(0, d3.event.x));
          foregroundDetail.attr("d", path);
          dimensions.sort(function(a, b) { return position(a) - position(b); });
          x.domain(dimensions);
          g.attr("transform", function(d) { return "translate(" + position(d) + ")"; })
        })
        .on("dragend", function(d) {
          delete dragging[d];
          transition(d3.select(this)).attr("transform", "translate(" + x(d) + ")");
          transition(foregroundDetail).attr("d", path);
          backgroundDetail
              .attr("d", path)
            .transition()
              .delay(500)
              .duration(0)
              .attr("visibility", null);
        }));

  // Adds the axis and title.
  g.append("g")
      .attr("class", "axis")
      .each(function(d) { d3.select(this).call(axis.scale(y[d])); })
    .append("text")
      .style("text-anchor", "middle")
      .attr("y", -9)
      .text(function(d) { return d; });

  // Creates a brush for each axis.
  g.append("g")
      .attr("class", "brush")
      .each(function(d) {
        d3.select(this).call(y[d].brush = d3.svg.brush().y(y[d]).on("brushstart", brushstart).on("brush", brush));
      })
    .selectAll("rect")
      .attr("x", -8)
      .attr("width", 16);
});

function position(d) {
  var v = dragging[d];
  return v == null ? x(d) : v;
}

function transition(g) {
  return g.transition().duration(500);
}

// Gets line for each point of data.
function path(d) {
  return line(dimensions.map(function(p) { return [position(p), y[p](d[p])]; }));
}

function brushstart() {
  d3.event.sourceEvent.stopPropagation();
}

// Turns off non selected lines.
function brush() {
  var actives = dimensions.filter(function(p) { return !y[p].brush.empty(); }),
      other = actives.map(function(p) { return y[p].brush.extent(); });
  foregroundDetail.style("display", function(d) {
    return actives.every(function(p, i) {
      return other[i][0] <= d[p] && d[p] <= other[i][1];
    }) ? null : "none";
  });
}

</script>