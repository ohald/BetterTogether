//dimensions of the svg
var width = 600, height = 600;

var radius = width / 20;

//test-users
var users = [
     {name: 'magnleik', image: "../images/magnleik.png", pairprog: 1},
     {name: 'esog', image: "../images/esog.png", pairprog: 1},
     {name: 'ohald', image: "../images/ohald.png", pairprog: 3},
];

//test-pair-entries
var edge_users = [
    {source: 0, target: 1},
    {source: 0, target: 2},
    {source: 1, target: 2},
]

//Add nodes to <class>-es
var node = d3.select('#circles')
        .selectAll('circle')
        .data(users)
        .enter().append('circle')
        .attr('class','user_node')
        .attr('r', radius)
        .attr('fill', function(d){
        return 'url(#'+d.name+')'})

//Add patterns to images
var defs = d3.select('#patterns_svg')
        .selectAll('pattern')
        .data(users)
        .enter()
        .append('pattern')
        .attr('id',function(d){
        return d.name})
        .attr('height', "100%")
        .attr('width', "100%")
        .attr('patternContentUnits','objectBoundingBox')
        .append('image')
        .attr('height',1)
        .attr('width',1)
        .attr('preserveAspectRatio','none')
        .attr('href', function(d){
        return d.image})


var simulation = d3.forceSimulation(users)
    .force('anticollide', d3.forceCollide(100))
    //.force('charge', d3.forceManyBody().strength(-1000))
    .force('center', d3.forceCenter(width/2, height/2))
    .force('link', d3.forceLink().links(edge_users))
    .on('tick', ticked);

function updateLinks() {
    var class_link = d3.select('#links')
        .selectAll('line')
        .data(edge_users)

    class_link.enter()
        .append('line')
        .merge(class_link)
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
    class_link.exit().remove()
}

function updateNodes() {
  node.enter()
    .append('circle')
    .attr('r', radius)
    .merge(node)
    .attr('cx', function(d) {
      return d.x
    })
    .attr('cy', function(d) {
      return d.y
    })
    .on('mouseover', handleMouseOver)
    .on('mouseout', handleMouseOut)
  node.exit().remove()
}

function ticked() {
  updateLinks()
  updateNodes()
}

function handleMouseOver(d, i){
  d3.select(this)
  .attr("r", 2 * radius);

  d3.select('svg').append('text')
  .attr('id', 'object_selected')
  .attr('x', function() { return d.x + radius*2.1; })
  .attr('y', function() { return d.y - height/35; })
  .text(function() { return d.name;});
}

function handleMouseOut(d, i){
  d3.select(this)
  .attr('r', radius)
  d3.select('#object_selected').remove();
}