//dimensions of the svg
var width = 600, height = 600;

var radius = width / 60;

//test-data
var nodes = [
     {name: 'A'},
     {name: 'B'},
     {name: 'C'},
     {name: 'D'},
     {name: 'E'},
];

var edges = [
    {source: 0, target: 1},
    {source: 0, target: 2},
    {source: 0, target: 4},
    {source: 1, target: 2},
    {source: 2, target: 4},
    {source: 3, target: 4}
]

var simulation = d3.forceSimulation(nodes)
.force('charge', d3.forceManyBody().strength(-1000))
.force('center', d3.forceCenter(width/2, height/2))
.force('link', d3.forceLink().links(edges))
.on('tick', ticked);

function updateLinks() {
  var u = d3.select('.links')
    .selectAll('line')
    .data(edges)

  u.enter()
    .append('line')
    .merge(u)
    .attr('x1', function(d) {
      return d.source.x
    })
    .attr('y1', function(d) {
      return d.source.y
    })
    .attr('x2', function(d) {
      return d.target.x
    })
    .attr('y2', function(d) {
      return d.target.y
    })

  u.exit().remove()
}

function updateNodes() {
  u = d3.select('svg')
    .selectAll('circle')
    .data(nodes)

  u.enter()
    .append('circle')
    .attr('r', radius)
    .merge(u)
    .attr('cx', function(d) {
      return d.x
    })
    .attr('cy', function(d) {
      return d.y
    })
    .on('mouseover', handleMouseOver)
    .on('mouseout', handleMouseOut)
  

  u.exit().remove()
}

function ticked() {
  updateLinks()
  updateNodes()
}

function handleMouseOver(d, i){
  d3.select(this)
  .attr('r', 2 * radius);

  d3.select('svg').append('text')
  .attr('id', 't' + i)
  .attr('x', function() { return d.x + 6; })
  .attr('y', function() { return d.y - height/35; })
  .text(function() { return d.name;});
}

function handleMouseOut(d, i){
  d3.select(this)
  .attr('r', radius)
  d3.select('#t' + i).remove();
}
