/** 
 * @fileoverview open Flash chart with javascript
 * copyleft 2005 {@link http://cafen.net cafen.net} || Copyright (C) 2007 John Glazebrook {@link http://teethgrinder.co.uk/open-flash-chart/ open-flash-chart}
 * @author Kimjonggab (outmind@cafen.net)
 * @version 1.0
 */

/**
 * extend Class || set parent object
 *
 * @param {object}  destination The destination object
 * @param {object}  source The source object to copy
 * @return {object}  The result object
 */
Object.setParent = function(destination, source) {
  for (property in source) {
  	if (typeof destination[property] == 'undefined')
    	destination[property] = source[property];
  }
  return destination;
}


/**
 * like php rand function
 *
 * @param {int}  min The minimum value of random number
 * @param {int}  max The maximum value of random number
 * @return {int}  The random value
 */
function rand(min, max) {
	return Math.round(Math.random() * (max - min)+ min);
}

if(typeof _chart_swf_path == "undefined") var _chart_swf_path = 'http://chart.cafen.net/JS/';
if(typeof _chart_server_path == "undefined") var _chart_server_path = 'http://chart.cafen.net/';

if(typeof showGraph == "undefined") var showGraph = new Object();



/**
 * @class show Chart or control Chart
 * @constructor 
 * @param {int}  w width
 * @param {int}  h height
 * @param {string}  ckey  site key
 */
showGraph = function(w, h, ckey) {
	this.setSize(w,h);
	this.setKey(ckey);
}

showGraph.prototype = {

	/**
	 * The width of flash object in pixel or percentage.
	 *
	 * @type string
	 */
	_width : '100%',

	/**
	 * The hieght of flash object in pixel or percentage.
	 *
	 * @type string
	 */
	_height : '100%',

	/**
	 * The site key on server. Muse be [a-z0-9_]. cannot use special char. and The length of sitekey is between 3 and 10.
	 *
	 * @type string
	 */
	_userkey : null,
/**
 * set Size
 *
 * @param {int}  w width
 * @param {int}  h height
 */
	setSize : function(w, h) {
		this._width = (w == null) ? '100%' : w ;
		this._height = (h == null) ? '100%' : h ;
	},
/**
 * set Site key
 *
 * @param {string}  ckey sitekey
 */
	setKey : function(ckey) {
		this._userkey = (ckey != null)? ckey : '';
	},
/**
 * render SWF Object
 *
 * @param {string}  objId object id
 * @param {string}  query the url that contain a chart data
 */
	renderObj : function(objId, query) {
		var chartobj = new SWFObject(_chart_swf_path +'open-flash-chart.swf', 'ofc', this._width, this._height, 9, "#FFFFFF");
		chartobj.addVariable("data", _chart_server_path +'?'+ encodeURIComponent(query));
		chartobj.write(objId);
	},
/**
 * register site key
 *
 * @param {string}  objId object id
 * @param {string}  userpw site chart password 
 * @param {string}  useremail site administrator email address
 */
	registerKey : function(objId, userpw, useremail) {
		this.renderObj(objId,'mode=REG&key='+this._userkey+'&pass='+userpw+'&email='+useremail);
	},
/**
 * modify site info.
 *
 * @param {string}  objId object id
 * @param {string}  userpw old site chart password 
 * @param {string}  newpw new site chart password 
 * @param {string}  useremail site administrator email address
 */
	modifyKey : function(objId, userpw, newpw, useremail) {
		this.renderObj(objId,'mode=MOD&key='+this._userkey+'&pass='+userpw+'&newpw='+newpw+'&email='+useremail);
	},
/**
 * save chart on server
 *
 * @param {string}  objId object id
 * @param {graph}  obj target graph object 
 * @param {string}  chartID chart id 
 * @param {string}  pass site chart password 
 */
	save : function(objId, obj, chartID, pass) {
		this.renderObj(objId,'&mode=SAVE&key='+this._userkey+'&pass='+pass+'&id='+chartID+obj.render());
	},
/**
 * render the chart from server
 *
 * @param {string}  objId object id
 * @param {string}  chartID chart id 
 * @param {string}  showType show type of chart [F : iframe, O : swf Object] 
 * @param {int}  w chart width
 * @param {int}  h chart height
 * @param {string}  userpass open chart password that was encrypted by crypt(PHP function) (use string as siteket + '_' + chartID and salt as site password)
 */
	render : function(objId, chartID, showType, w, h, userpass) {
		try {var checkimg = new Image();	checkimg.src= 'http://cafen.net/solution/editormonitor.html?OFC1.0';} catch(ex){}
		if  (w != null && h != null) 
			this.setSize(w,h);
		if (chartID.indexOf('@') > 0)
			chartID += userpass;
		switch(showType) {
			case 'F' :
				var layObj = (typeof objId == 'string') ? document.getElementById(objId) : objId;
				var obj = document.createElement('iframe');
				obj.frameBorder ='0';
				obj.allowTransparency ='1';
				 obj.scrolling="no";
				obj.src = _chart_server_path + this._userkey+'/'+chartID;
				layObj.appendChild(obj);
				obj.style.width = this._width;
				obj.style.height = this._height;
				break;
			default :
				this.renderObj(objId,'key='+this._userkey+'&id='+chartID);
				break;
		}
	}
}

if(typeof graph == "undefined") var graph = new Object();

/**
 * @class make graph class
 * @constructor 
 * @param {int}  w width
 * @param {int}  h height
 */
graph = function(w, h) {
	this.data_sets = [];
	this.data = [];
	this.links = [];
	this.width = w;
	this.height = h;
	this.js_path = 'js/';
	this.swf_path = _chart_swf_path;
	this.x_labels = [];
	this.y_min = null;
	this.y_max = null;
	this.x_min = null;
	this.x_max = null;
	this.y_steps = '';
	this.title_txt = '';
	this.title_style = '';
	this.occurence = 0;
	this.x_offset = '';
	this.x_tick_size = -1;
	this.y2_max = null;
	this.y2_min = null;

	// GRID styles:
	this._x_axis_colour = '';
	this._x_axis_3d = '';
	this._x_grid_colour = '';
	this._x_axis_steps = 1;
	this._y_axis_colour = '';
	this._y_grid_colour = '';
	this.y2_axis_colour = '';
	
	// AXIS LABEL styles:         
	this.x_label_style = '';
	this.y_label_style = '';
	this.y_label_style_right = '';


	// AXIS LEGEND styles:
	this.x_legend = '';
	this.x_legend_size = 20;
	this.x_legend_colour = '#000000';

	this.y_legend = '';
	this.y_legend_right = '';
	//this.y_legend_size = 20;
	//this.y_legend_colour = '#000000';

	this.lines = [];
	this.line_default = {};
	this.line_default['type'] = 'line';
	this.line_default['values'] = '3,#87421F';
	this.js_line_default = 'so.addVariable("line","3,#87421F");';
	
	this.bg_colour = '';
	this.bg_image = '';

	this.inner_bg_colour = '';
	this.inner_bg_colour_2 = '';
	this.inner_bg_angle = '';
	
	// PIE chart ------------
	this._pie = '';
	this._pie_values = '';
	this._pie_colours = '';
	this._pie_labels = '';
	this._pie_links = '';
	
	this.tool_tip = '';
	
	// which data lines are attached to the
	// right Y axis?
	this.y2_lines = [];
	
	// Number formatting:
	this.y_format='';
	this.num_decimals='';
	this.is_fixed_num_decimals_forced='';
	this.is_decimal_separator_comma='';
	this.is_thousand_separator_disabled='';
	this.output_type = '';
	this.set_output_type('js');	
	
	//
	// set some default value incase the user forgets
	// to set them, so at least they see *something*
	// even is it is only the axis and some ticks
	//
	this.set_y_min( 0 );
	this.set_y_max( 20 );
	this.set_x_axis_steps( 1 );
	this.y_label_steps( 5 );
}

graph.prototype = {

	/**
	 * The Array of data point object
	 *
	 * @type array
	 */
	data_sets : [],

	/**
	 * The Array of data point
	 *
	 * @type array
	 */
	data : [],

	/**
	 * The Array of link
	 *
	 * @type array
	 */
	links : [],

	/**
	 * The width of chart Object in pixel or percentage
	 *
	 * @type mixed
	 */
	width : '100%',

	/**
	 * The width of chart Object in pixel or percentage
	 *
	 * @type mixed
	 */
	height : '100%',

	/**
	 * The javascript Path 
	 *
	 * @type string
	 */
	js_path : 'js/',

	/**
	 * The Flash Chart path of [open-flash-chart.swf] file that be located 
	 *
	 * @type string
	 */
	swf_path : _chart_swf_path,

	/**
	 * The array of x axis labels
	 *
	 * @type array
	 */
	x_labels : [],

	/**
	 * The minimum value of x axis in int or float
	 *
	 * @type number
	 */
	y_min : null,

	/**
	 * The maximum value of x axis in int or float
	 *
	 * @type number
	 */
	y_max : null,

	/**
	 * The minimum value of y axis in int or float
	 *
	 * @type number
	 */
	x_min : null,

	/**
	 * The maximum value of y axis in int or float
	 *
	 * @type number
	 */
	x_max : null,

	/**
	 * The step of y axis labels
	 *
	 * @type int
	 */
	y_steps : null,

	/**
	 * The chart title string
	 *
	 * @type string
	 */
	title_txt : '',

	/**
	 * The chart title style. the format like this : {font-size:'10px', color : '#464646'}
	 *
	 * @type string
	 */
	title_style : '',

	/**
	 * not use (exactly I do'not know what is this)
	 *
	 * @type int
	 */
	occurence : 0,

	/**
	 * The offset of x axis
	 *
	 * @type int
	 */
	x_offset : '',

	/**
	 * The size of tick
	 *
	 * @type int
	 */
	x_tick_size : -1,

	/**
	 * The maximum value of right y axis in int or float
	 *
	 * @type int
	 */
	y2_max : null,

	/**
	 * The minimum value of right y axis in int or float
	 *
	 * @type int
	 */
	y2_min : null,

	/**
	 * The colour of x axis
	 *
	 * @type string
	 */
	_x_axis_colour : '',

	/**
	 * The size of x 3d axis
	 *
	 * @type int
	 */
	_x_axis_3d : 0,

	/**
	 * The colour of x grid
	 *
	 * @type string
	 */
	_x_grid_colour : '',

	/**
	 * The step of x axis
	 *
	 * @type int
	 */
	_x_axis_steps : 1,

	/**
	 * The colour of y axis
	 *
	 * @type string
	 */
	_y_axis_colour : '',

	/**
	 * The colour of y grid
	 *
	 * @type string
	 */
	_y_grid_colour : '',

	/**
	 * The colour of y right axis
	 *
	 * @type string
	 */
	y2_axis_colour : '',
	
	/**
	 * The style of x axis labels
	 *
	 * @type string
	 */
	x_label_style : '',

	/**
	 * The style of y axis labels
	 *
	 * @type string
	 */
	y_label_style : '',

	/**
	 * The style of y right axis labels
	 *
	 * @type string
	 */
	y_label_style_right : '',

	/**
	 * The legend of x axis
	 *
	 * @type string
	 */
	x_legend : '',

	/**
	 * The font size of x axis legend
	 *
	 * @type int
	 */
	x_legend_size : 20,

	/**
	 * The colour of x axis legend
	 *
	 * @type string
	 */
	x_legend_colour : '#000000',

	/**
	 * The legend of y axis
	 *
	 * @type string
	 */
	y_legend : '',

	/**
	 * The legend of y right axis
	 *
	 * @type string
	 */
	y_legend_right : '',

	/**
	 * The array of data point
	 *
	 * @type array
	 */
	lines : [],

	/**
	 * The object that be use in case the data length is 0
	 *
	 * @type object
	 */
	line_default : {
		'type' : 'line',
		'values' : '3,#87421F'
	},

	/**
	 * not use
	 *
	 * @type string
	 */
	js_line_default : 'so.addVariable("line","3,#87421F"),',
	
	/**
	 * The colour of chart background
	 *
	 * @type string
	 */
	bg_colour : '',

	/**
	 * The url of background image
	 *
	 * @type string
	 */
	bg_image : '',

	/**
	 * The 1st colour of chart inner background
	 *
	 * @type string
	 */
	inner_bg_colour : '',

	/**
	 * The 2nd colour of chart inner background
	 *
	 * @type string
	 */
	inner_bg_colour_2 : '',

	/**
	 * The angle of chart inner background
	 *
	 * @type int
	 */
	inner_bg_angle : -1,
	
	/**
	 * The Pie chart informat that contaion chart title and font size etc.
	 *
	 * @type string
	 */
	_pie : '',

	/**
	 * The Pie chart data that joined by ',' .
	 *
	 * @type string
	 */
	_pie_values : '',

	/**
	 * The colours of Pie chart that joined by ',' .
	 *
	 * @type string
	 */
	_pie_colours : '',

	/**
	 * The labels of Pie chart that joined by ',' .
	 *
	 * @type string
	 */
	_pie_labels : '',

	/**
	 * The links of Pie chart that joined by ',' .
	 *
	 * @type string
	 */
	_pie_links : '',
	
	/**
	 * The Tool tip.
	 *
	 * @type string
	 */
	tool_tip : '',
	
	/**
	 * I don't know ^^;
	 *
	 * @type array
	 */
	y2_lines : [],

	/**
	 * I don't know ^^;
	 *
	 * @type string
	 */
	y_format:'',

	/**
	 * I don't know ^^;
	 *
	 * @type string
	 */
	num_decimals:'',

	/**
	 * I don't know ^^;
	 *
	 * @type string
	 */
	is_fixed_num_decimals_forced:'',

	/**
	 * I don't know ^^;
	 *
	 * @type string
	 */
	is_decimal_separator_comma:'',

	/**
	 * I don't know ^^;
	 *
	 * @type string
	 */
	is_thousand_separator_disabled:'',

	/**
	 * The format of output;
	 *
	 * @type string
	 */
	output_type : 'js',

	/**
	* Set the unique_id to use for the flash object id.
	* @param {string}  uniqid uniqid that will be placed chart
	*/
	set_unique_id : function(uniqid) {
		if (uniqid == null)
			this.unique_id = '_'+ Math.floor(Math.random() * 99999+ 10000) + '_' +Math.floor(Math.random() * 99999 + 10000);
		else
			this.unique_id = uniqid;
	},

	/**
	* Get the flash object ID for the last rendered object.
	* @return {string}  uniqid 
	*/
	get_unique_id : function() {
		return this.unique_id;
	},

	/**
	* Set the base path for the swfobject.js
	*
	* @param {string} path a string argument.
	*   The path to the swfobject.js file
	*/
	set_js_path : function(path) {
		this.js_path = path;
	},

	/**
	* Set the base path for the open-flash-chart.swf
	*
	* @param {string} path a string argument.
	*   The path to the open-flash-chart.swf file
	*/
	set_swf_path : function(path) {
		this.swf_path = path;
	},

	/**
	* Set the type of output data.
	*
	* @param {string} type a string argument.
	*   The type of data.  Currently only type is js, or nothing.
	*/
	set_output_type : function(type) {
		if (type == null)
			type = 'js';
		this.output_type = type;
	},

	/**
	* returns the next line label for multiple lines.
	*
	* @return {string}  the next line label for multiple lines
	*/
	next_line : function() {
		var line_num = '';
		if( this.lines.length > 0 )
			line_num = '_' + (this.lines.length+1);
		return line_num;
	},

	/**
	* escape commas (,)
	* we replace the comma so it is not URL escaped
	* if it is, flash just thinks it is a comma
	* which is no good if we are splitting the
	* string on commas.
	*
	* @return {string}  escape commas (,)
	*/
	esc : function( text ) {
		if (typeof text == 'number')
			text = text.toString();
		var tmp = text.replace( ",", "#comma#");
		try {
			return encodeURI(tmp);
		} catch(ex) {
			return text;
		}
	},

	/**
	* Format the text to the type of output.
	*
	* @param {string}  key flash param  key name
	* @param {string}  values flash param values
	* @return {string}  data params
	*/
	format_output : function(key,values){
		if(this.output_type == 'js')	
			this.so.addVariable(key,values);
		return [key, values];
	},

	/**
	* Set the text and style of the title.
	*
	* @param {string} title The text of the title.
	* @param {string} style CSS styling of the title.
	*/
	set_title : function(title, style){
		this.title_txt = this.esc( title );
		if(style != null && style.length > 0 )
			this.title_style = style;
	},

	/**
	 * Set the width of the chart.
	 *
	 * @param {int} width The width of the chart frame.
	 */
	set_width : function( width ){
		this.width = width;
	},
	
	/**
	 * Set the height of the chart.
	 *
	 * @param {int} height The height of the chart frame.
	 */
	set_height : function( height ){
		this.height = height;
	},

	/**
	 * Set the base path of the swfobject.
	 *
	 * @param {string} base  The base path of the swfobject.
	 */
	set_base : function( base ){
		if (base == null) base = 'js/';
		this.base = base;
	},
	
	/**
	 * Number formatting
	 *
	 * @param {string} val  Number formatting.
	 */
	set_y_format : function( val ){
		this.y_format = val;	
	},

	/**
	 * Number formatting
	 *
	 * @param {string} val  Number formatting.
	 */
	set_num_decimals : function( val ){
		this.num_decimals = val;
	},
	
	/**
	 * Number formatting
	 *
	 * @param {string} val  Number formatting.
	 */
	set_is_fixed_num_decimals_forced : function( val ){
		this.is_fixed_num_decimals_forced = val? 'true': 'false';
	},
	
	/**
	 * Number formatting
	 *
	 * @param {string} val  Number formatting.
	 */
	set_is_decimal_separator_comma : function( val ) {
		this.is_decimal_separator_comma = val?'true':'false';
	},
	
	/**
	 * Number formatting
	 *
	 * @param {string} val  Number formatting.
	 */
	set_is_thousand_separator_disabled : function( val ) {
		this.is_thousand_separator_disabled = val ? 'true':'false';
	},

	/**
	 * Set the data for the chart
	 *
	 * @param {array} a An array of the data to add to the chart.
	 */
	set_data : function( a ){
		this.data.push(a.join(","));
	},
	
	/**
	 * UGH, these evil functions are making me fell ill
	 *
	 * @param {array} links An array of the link to add the chart.
	 */
	set_links : function(links ){
		// TO DO escape commas:
		this.links.push(links.join(","));
	},

	/**
	 * X offset 
	 *
	 * @param {boolean} val .
	 */
	set_x_offset : function( val )	{
		this.x_offset = val ? 'true':'false';
	},

	/**
	 * Set the tooltip to be displayed on each chart item.
	 *
	 * Replaceable tokens that can be used in the string include: 
	 * #val# - The actual value of whatever the mouse is over. 
	 * #key# - The key string. 
	 * \<br>  - New line.
	 * #x_label# - The X label string.
	 * #x_legend# - The X axis legend text. 
	 * Default string is: "#x_label#<br>#val#"
	 * 
	 * @param {string} tip A formatted string to show as the tooltip.
	 */
	set_tool_tip : function( tip ) {
		this.tool_tip = this.esc( tip );
	},

	/**
	 * Set the x axis labels
	 *
	 * @param {array} a An array of the x axis labels.
	 */
	set_x_labels : function( a ){
		var tmp = [];
		for(var i = 0 ; i < a.length; i++) 
			tmp.push(this.esc( a[i]));
		this.x_labels = tmp;
	},

	/**
	 * Set the look and feel of the x axis labels
	 *
	 * @param {int} size The font size.
	 * @param {string} colour  The hex colour value.
	 * @param {int} orientation The orientation of the x-axis text.
	 *   0 - Horizontal
	 *   1 - Vertical
	 *   2 - 45 degrees
	 * @param {int} step Show the label on every step label.
	 * @param {string} grid_colour The hex colour value of grids.
	 */
	set_x_label_style : function( size, colour, orientation, step, grid_colour){
		if (colour == null)
			colour = '';
		if (orientation == null)
			orientation = 0;
		if (step == null)
			step = -1;
		if (grid_colour == null)
			grid_colour = '';
			
		this.x_label_style = size;
		
		if(colour.length > 0 )
			this.x_label_style += ','+ colour;

		if( orientation > -1 )
			this.x_label_style += ','+ orientation;

		if( step > 0 )
			this.x_label_style += ','+ step;

		if( grid_colour.length > 0 )
			this.x_label_style += ','+ grid_colour;
	},

	/**
	 * Set the background colour.
	 *
	 * @param {string} colour The hex colour value.
	 */
	set_bg_colour : function( colour ){
		this.bg_colour = colour;
	},

	/**
	 * Set a background image.
	 *
	 * @param {string} url The location of the image.
	 * @param {string} x The x location of the image. 'Right', 'Left', 'Center'
	 * @param {string} y The y location of the image. 'Top', 'Bottom', 'Middle'
	 */
	set_bg_image : function( url, x, y ){
		if (x == null)
			x = 'center';
		if (y == null)
			y = 'center';
			
		this.bg_image = url;
		this.bg_image_x = x;
		this.bg_image_y = y;
	},

	/**
	 * Attach a set of data (a line, area or bar chart) to the right Y axis.
	 *
	 * @param {int} data_number The numbered order the data was attached using set_data.
	 */
	attach_to_y_right_axis : function( data_number ){
		this.y2_lines.push(data_number);
	},

	/**
 	 * Set the background colour of the grid portion of the chart.
 	 *
	 * @param {string} col The hex colour value of the background.
	 * @param {string} col2 The hex colour value of the second colour if you want a gradient.
	 * @param {int} angle The angle in degrees to make the gradient.
	 */
	set_inner_background : function( col, col2, angle){
		this.inner_bg_colour = col;
		if( col2 != null &&  col2.length > 0 )
			this.inner_bg_colour_2 = col2;
		if( angle != null && angle != -1 )
			this.inner_bg_angle = angle;
	},

	/**
	 * Internal function to build the y label style for y and y2
	 *
	 * @param {int} size The font size.
	 * @param {string} colour The hex colour value.
	 */
	_set_y_label_style : function( size, colour ) {
		var tmp = size;
		
		if(colour != null && colour.length > 0 )
			tmp += ','+ colour;
		return tmp;
	},

	/**
	 * Set the look and feel of the y axis labels
	 *
	 * @param {int} size The font size.
	 * @param {string} colour The hex colour value.
	 */
	set_y_label_style : function( size, colour) {
		this.y_label_style = this._set_y_label_style( size, colour );
	},

	/**
	 * Set the look and feel of the right y axis labels
	 *
	 * @param font_size an int argument.
	 *   The font size.
	 * @param colour a string argument.
	 *   The hex colour value.
	 */
	set_y_right_label_style : function( size, colour ){
		this.y_label_style_right = this._set_y_label_style( size, colour );
	},

	/**
	 * Set the maximum value of  x axis
	 *
	 * @param {float} max max value.
	 */
	set_x_max : function( max ){
		this.x_max = parseFloat( max );
	},

	/**
	 * Set the minimum value of the x axis.
	 *
	 * @param {float} min The minimum value.
	 */
	set_x_min : function( min ){
		this.x_min = parseFloat( min );
	},

	/**
	 * Set the maximum value of the y axis.
	 *
	 * @param {float} max The maximum value.
	 */
	set_y_max : function( max ){
		this.y_max = parseFloat( max );
	},

	/**
	 * Set the minimum value of the y axis.
	 *
	 * @param {float} min The minimum value.
	 */
	set_y_min : function( min ) {
		this.y_min = parseFloat( min );
	},

	/**
	 * Set the maximum value of the right y axis.
	 *
	 * @param {float} max The maximum value.
	 */  
	set_y_right_max : function( max )	{
		this.y2_max = parseFloat(max);
	},

	/**
	 * Set the minimum value of the right y axis.
	 *
	 * @param min an float argument.
	 *   The minimum value.
	 */
	set_y_right_min : function( min ){
		this.y2_min = parseFloat(min);
	},

	/**
	 * Show the y label on every $step label.
	 *
	 * @param {int} val Show the label on every step label.
	 */
	y_label_steps : function( val ){
		 this.y_steps = parseInt( val );
	},

	/**
	* Alias of set Title.
	*
	* @param {string} title The text of the title.
	* @param (string} style CSS styling of the title.
	*/
	title : function( title, style)	{
		this.set_title(title, style);
	},

	/**
	 * Set the parameters of the x legend.
	 *
	 * @param {string} text The text of the x legend.
	 * @param {int} size The font size of the x legend text.
	 * @param {string} colour  The hex value of the font colour. 
	 */
	set_x_legend : function( text, size, colour){
		this.x_legend = this.esc( text );
		if(size != null && size > -1 )
			this.x_legend_size = size;
		
		if(colour !=null && colour.length >0 )
			this.x_legend_colour = colour;
	},

	/**
	 * Set the size of the x label ticks.
	 *
	 * @param {int} size The size of the ticks in pixels.
	 */
	set_x_tick_size : function( size ){
		if( size > 0 )
			this.x_tick_size = size;
	},

	/**
	 * Set how often you would like to show a tick on the x axis.
	 *
	 * @param {int} steps Show a tick ever $steps.
	 */
	set_x_axis_steps : function( steps ){
		if ( steps > 0 )
			this._x_axis_steps = steps;
	},

	/**
	 * Set the depth in pixels of the 3D X axis slab.
	 *
	 * @param {int} size The depth in pixels of the 3D X axis.
	 */
	set_x_axis_3d : function( size ){
		if( size > 0 )
			this._x_axis_3d = parseInt(size);
	},
	
	/**
	 * The private method of building the y legend output.
	 *
	 * @param {string} text a The text of the y legend.
	 * @param {int} size  The font size of the y legend text.
	 * @param {string} colour The hex colour value of the font colour. 
	 */
	_set_y_legend : function( text, size, colour ){
		var tmp = text;
	
		if(size != null && size > -1 )
			tmp += ','+ size;

		if(colour != null && colour.length >0 )
			tmp += ',' + colour;

		return tmp;
	},

	/**
	 * Set the parameters of the y legend.
	 *
	 * @param {string} text a The text of the y legend.
	 * @param {int} size  The font size of the y legend text.
	 * @param {string} colour The hex colour value of the font colour. 
	 */
	set_y_legend : function( text, size, colour ){
		this.y_legend = this._set_y_legend( text, size, colour);
	},

	/**
	 * Set the parameters of the right y legend.
	 *
	 * @param {string} text The text of the right y legend.
	 * @param {int} size The font size of the right y legend text.
	 * @param {string} colour The hex value of the font colour. 
	 */
	set_y_right_legend : function( text, size, colour ){
		this.y_legend_right = this._set_y_legend( text, size, colour );
	},
	
	/**
	 * Set the colour of the x axis line and grid.
	 *
	 * @param {string} axis The hex colour value of the x axis line.
	 * @param {string} grid The hex colour value of the x axis grid. 
	 */
	x_axis_colour : function( axis, grid ){
		this._x_axis_colour = axis;
		this._x_grid_colour = grid;
	},

	/**
	 * Set the colour of the y axis line and grid.
	 *
	 * @param {string} axis The hex colour value of the y axis line.
	 * @param {string} grid The hex colour value of the y axis grid. 
	 */
	y_axis_colour : function( axis, grid){
		this._y_axis_colour = axis;
		if(grid != null && grid.length > 0 )
			this._y_grid_colour = grid;
	},

	/**
	 * Set the colour of the right y axis line.
	 *
	 * @param {string} colour The hex colour value of the right y axis line.
	 */
	y_right_axis_colour : function( colour ){
		 this.y2_axis_colour = colour;
	},

	/**
	 * Draw a line without markers on values.
	 *
	 * @param {int} width The width of the line in pixels.
	 * @param {string} colour The hex colour value of the line.
	 * @param {string} text The label of the line.
	 * @param {int} size Font size of the label
	 * @param {int} circles Need to find out.
	 */
	line : function( width, colour, text , size , circles){
		var type = 'line'+ this.next_line();
		var description = '';
		if( width > 0 ){
			description += width;
			description += ','+ colour;
		}
		if( text.length > 0 )	{
			description += ','+ text;
			description += ','+ size;
		}
		if(circles != null && circles > 0 ) 
			description += ','+ circles;
		this.lines.push([type,description]);
	},

	/**
	 * Draw a line with solid dot markers on values.
	 *
	 * @param {int} width The width of the line in pixels.
	 * @param {int} dot_size Size in pixels of the dot.
	 * @param {string} colour The hex colour value of the line.
	 * @param {string} text The label of the line.
	 * @param {int} font_size Font size of the label.
	 */
	line_dot : function( width, dot_size, colour, text, font_size){
		var type = 'line_dot'+ this.next_line();
		var description = width+','+colour+','+text;
		if(font_size != null && font_size  > 0 )
			description += ','+font_size+','+dot_size;
		this.lines.push([type,description]);
	},

	/**
	 * Draw a line with hollow dot markers on values.
	 *
	 * @param {int} width The width of the line in pixels.
	 * @param {int} dot_size Size in pixels of the dot.
	 * @param {string} colour The hex colour value of the line.
	 * @param {string} text The label of the line.
	 * @param {int} font_size Font size of the label.
	 */
	line_hollow : function( width, dot_size, colour, text, font_size){
		var type = 'line_hollow'+ this.next_line();
		var description = width+','+colour+','+text;
		if(font_size != null && font_size > 0 )
			description += ','+font_size+','+dot_size;
		this.lines.push([type,description]);
	},

	/**
	 * Draw an area chart.
	 *
	 * @param {int} width The width of the line in pixels.
	 * @param {int} dot_size Size in pixels of the dot.
	 * @param {int} alpha The percentage of transparency of the fill colour.
	 * @param {string} colour The hex colour value of the line.
	 * @param {string} text The label of the line.
	 * @param {int} font_size Font size of the label.
	 * @param {string} fill_colour The hex colour value of the fill colour.
	 */
	area_hollow : function( width, dot_size, alpha, colour, text,  font_size , fill_colour) {
		var type = 'area_hollow' + this.next_line();
		var description = width+','+dot_size+','+alpha+','+colour;
		if(text !=null && text.length > 0 )
			description += ','+text+','+font_size;
		else
			description += ',,';
		if(fill_colour !=null && fill_colour.length > 0 )
			description += ','+ fill_colour;
		this.lines.push([type,description]);
	},

	/**
	 * Draw a bar chart.
	 *
	 * @param {int} alpha The percentage of transparency of the bar colour.
	 * @param {string} colour The hex colour value of the line.
	 * @param {string} text The label of the line.
	 * @param {int} size Font size of the label.
	 */
	bar : function( alpha, colour, text, size){
		var type = 'bar'+ this.next_line();
		var description = alpha +','+ colour +','+ text +','+ size;
		this.lines.push([type,description]);
	},

	/**
	 * Draw a bar chart with an outline.
	 *
	 * @param {int} alpha The percentage of transparency of the bar colour.
	 * @param {string} colour The hex colour value of the line.
	 * @param {string}colour_outline The hex colour value of the outline.
	 * @param {string} text The label of the line.
	 * @param {int} size Font size of the label.
	 */
	bar_filled : function( alpha, colour, colour_outline, text, size){
		var type = 'filled_bar'+ this.next_line();
		var description = alpha+','+colour+','+colour_outline+','+text+','+size;
		this.lines.push([type,description]);
	},

	/**
	 * Draw a bar chart with a sketch.
	 *
	 * @param {int} alpha The percentage of transparency of the bar colour.
	 * @param {int} offset .
	 * @param {string} colour The hex colour value of the line.
	 * @param {string}colour_outline The hex colour value of the outline.
	 * @param {string} text The label of the line.
	 * @param {int} size Font size of the label.
	 */
// &bar_sketch=55,6,#d070ac,#000000,2006,10&
	bar_sketch : function( alpha, offset,colour, colour_outline, text, size){
		var type = 'bar_sketch' + this.next_line();
		var description = alpha+','+offset+','+colour+','+colour_outline+','+text+','+size;
		this.lines.push([type,description]);
	},

	/**
	 * Draw a 3D bar chart.
	 *
	 * @param {int} alpha The percentage of transparency of the bar colour.
	 * @param {string} colour The hex colour value of the line.
	 * @param {string} text The label of the line.
	 * @param {int} size Font size of the label.
	 */
	bar_3D : function( alpha, colour, text, size){
		var type = 'bar_3d'+ this.next_line();
		var description = alpha +','+ colour +','+ text +','+ size;
		this.lines.push([type,description]);
	},

	/**
	 * Draw a 3D bar chart that looks like glass.
	 *
	 * @param {int} alpha The percentage of transparency of the bar colour.
	 * @param {string} colour The hex colour value of the line.
	 * @param {string} outline_colour The hex colour value of the outline.
	 * @param {string} text The label of the line.
	 * @param {int} size Font size of the label.
	 */
	bar_glass : function( alpha, colour, outline_colour, text, size ){
		var type = 'bar_glass'+ this.next_line();
		var description = alpha +','+ colour +','+ outline_colour +','+ text +','+ size;
		this.lines.push([type,description]);
	},

	/**
	 * Draw a faded bar chart.
	 *
	 * @param {int} alpha The percentage of transparency of the bar colour.
	 * @param {string} colour The hex colour value of the line.
	 * @param {string} text The label of the line.
	 * @param {int} size Font size of the label.
	 */
	bar_fade : function( alpha, colour, text, size )	{
		var type = 'bar_fade'+ this.next_line();
		var description = alpha +','+ colour +','+ text +','+ size;
		this.lines.push([type,description]);
	},
	
	/**
	 * Draw a candle chart.
	 *
	 * @param {point} data the array of data point set
	 * @param {int} alpha The percentage of transparency of the bar colour.
	 * @param {int} line_width The width  of line in pixel.
	 * @param {string} colour The hex colour value of the line.
	 * @param {string} text The label of the line.
	 * @param {int} size Font size of the label.
	 */
	candle : function( data,  alpha, line_width, colour, text, size){
		var type = 'candle'+ this.next_line();
		var description = alpha +','+ line_width +','+ colour +','+ text +','+ size;
		this.lines.push([type,description]);
		
		var a = [];
		for(var i = 0; i < data.length; i++) 
			a.push(data[i].toString());
		this.data.push(a.join(','));
	},
	
	/**
	 * Draw a high and low chart.
	 *
	 * @param {point} data the array of data point set
	 * @param {int} alpha The percentage of transparency of the bar colour.
	 * @param {int} line_width The width  of line in pixel.
	 * @param {string} colour The hex colour value of the line.
	 * @param {string} text The label of the line.
	 * @param {int} size Font size of the label.
	 */
	hlc : function( data, alpha, line_width, colour, text, size){
		var type = 'hlc'+ this.next_line();
		var description = alpha +','+ line_width +','+ colour +','+ text +','+ size;
		this.lines.push([type,description]);
		var a = [];
		for(var i = 0; i < data.length; i++) 
			a.push(data[i].toString());
		this.data.push(a.join(','));
	},

	/**
	 * Draw a scatter chart.
	 *
	 * @param {point} data the array of data point set
	 * @param {int} alpha The percentage of transparency of the bar colour.
	 * @param {int} line_width The width  of line in pixel.
	 * @param {string} colour The hex colour value of the line.
	 * @param {string} text The label of the line.
	 * @param {int} size Font size of the label.
	 */
	scatter : function( data, line_width, colour, text, size) {
		var type = 'scatter' + this.next_line();
		var description = line_width +','+ colour +','+ text +','+ size;
		this.lines.push([type,description]);
		var a = [];
		for(var i = 0; i < data.length; i++) 
			a.push(data[i].toString());
		this.data.push(a.join(','));
	},

	/**
	 * Draw a pie chart.
	 * Patch by, Jeremy Miller (14th Nov, 2007)
	 *
	 * @param {int} alpha The percentage of transparency of the pie colour.
	 * @param {string} line_color The hex colour value of the line
	 * @param {string} style CSS style string
	 * @param {boolean} gradient Use a gradient true or false.
	 * @param {int} border_size Size of the border in pixels.
	 */
	pie : function( alpha, line_colour, style, gradient, border_size){
		this._pie = alpha+','+line_colour+','+style;
		if(gradient != null && gradient) 
			this._pie += ','+gradient;
		if (border_size) {
			if (gradient == null || gradient == false)
				this._pie += ',';
			this._pie += ',' + border_size;
		}
	},

	/**
	 * Set the values of the pie chart.
	 *
	 * @param {array} values An array of the values for the pie chart.
	 * @param {array} labels An array of the labels for the pie pieces.
	 * @param {array} links An array of the links to the pie pieces.
	 */	
	pie_values : function( values, labels , links){
		if (values !=null && values.length > 0)
			this._pie_values = values.join(',');
		if (labels !=null && labels.length > 0)
			this._pie_labels = labels.join(',');
		if (links !=null && links.length > 0)
			this._pie_links  = links.join(',');
	},

	/**
	 * Set the pie slice colours.
	 *
	 * @param {array} colours The hex colour values of the pie pieces.
	 */
	pie_slice_colours : function( colours ){
		this._pie_colours = colours.join(',');
	},
	
	/**
	 * Render the output.
	 *
	 * @param {string} uniqid the ID of object that will be placed
	 * @return {string} parameter of flash in string to send server
	 */
	render : function(uniqid){
		try {var checkimg = new Image();	checkimg.src= 'http://cafen.net/solution/editormonitor.html?OFC1.0';} catch(ex){}
		if (uniqid == null)
			this.set_output_type('save');	
		var tmp = [];
		
		this.set_unique_id(uniqid);
		if(this.output_type == 'js') {
			this.so = new SWFObject(this.swf_path +'open-flash-chart.swf', 'ofc', this.width, this.height, 9, "#FFFFFF");
			this.so.addVariable("variables","true");
		}
		var values = '';
		if( this.title_txt.length > 0 ) {
			values = this.title_txt;
			values += ','+ this.title_style;
			tmp.push(this.format_output('title',values));
		}

		if(this.x_legend.length > 0 ){
			values = this.x_legend;
			values += ','+ this.x_legend_size;
			values += ','+ this.x_legend_colour;
			tmp.push(this.format_output('x_legend',values));
		}
	
		if( this.x_label_style.length > 0 )
			tmp.push(this.format_output('x_label_style',this.x_label_style));
	
		if( this.x_tick_size > 0 )
			tmp.push(this.format_output('x_ticks',this.x_tick_size));

		if( this._x_axis_steps > 0 )
			tmp.push(this.format_output('x_axis_steps',this._x_axis_steps));

		if( this._x_axis_3d > 0 )
			tmp.push(this.format_output('x_axis_3d',this._x_axis_3d));
		
		if( this.y_legend.length > 0 )
			tmp.push(this.format_output('y_legend',this.y_legend));
		
		if( this.y_legend_right.length > 0 )
			tmp.push(this.format_output('y2_legend',this.y_legend_right));

		if( this.y_label_style.length > 0 )
			tmp.push(this.format_output('y_label_style',this.y_label_style));

		var values = '5,10,'+ this.y_steps;
		tmp.push(this.format_output('y_ticks',values));

		if( this.lines.length == 0 && this.data_sets.length == 0 ){
			tmp.push(this.format_output(this.line_default['type'],this.line_default['values']));	
		} else {
			for(var i =0; i < this.lines.length; i++) {
				var line = this.lines[i];
				tmp.push(this.format_output(line[0],line[1]));	
			}
		}
	
		var num = 1;
		for(var i = 0 ; i < this.data.length; i++) {
			var data = this.data[i];
			if( num==1 ) {
				tmp.push(this.format_output( 'values', data));
			}	else {
				tmp.push(this.format_output('values_'+ num, data));
			}
			num++;
		}
		num = 1;
		
		for(var i = 0 ; i < this.links.length; i++) {
			var link = this.links[i];
			if( num==1 ){
				tmp.push(this.format_output( 'links', link));
			}else{
				tmp.push(this.format_output('links_'+ num, link));
			}
			num++;
		}

		if( this.y2_lines.length > 0 ) {
			tmp.push(this.format_output('y2_lines',this.y2_lines.join(',')));
			//
			// Should this be an option? I think so...
			//
			tmp.push(this.format_output('show_y2','true'));
		}

		if( this.x_labels.length > 0 )
			tmp.push(this.format_output('x_labels',this.x_labels.join(',')));
		else {
			if(this.x_min != null )
				tmp.push(this.format_output('x_min',this.x_min));
				
			if(this.x_max != null)
				tmp.push(this.format_output('x_max',this.x_max));			
		}
		
		tmp.push(this.format_output('y_min',(this.y_min != null) ? this.y_min : ''));
		tmp.push(this.format_output('y_max',(this.y_max != null) ? this.y_max : ''));

		if(this.y2_min != null )
			tmp.push(this.format_output('y2_min',this.y2_min));
			
		if( this.y2_max != null )
			tmp.push(this.format_output('y2_max',this.y2_max));
		
		if( this.bg_colour.length > 0 )
			tmp.push(this.format_output('bg_colour',this.bg_colour));

		if( this.bg_image.length > 0 ){
			tmp.push(this.format_output('bg_image',this.bg_image));
			tmp.push(this.format_output('bg_image_x',this.bg_image_x));
			tmp.push(this.format_output('bg_image_y',this.bg_image_y));
		}

		if( this._x_axis_colour.length > 0 )	{
			tmp.push(this.format_output('x_axis_colour',this._x_axis_colour));
			tmp.push(this.format_output('x_grid_colour',this._x_grid_colour));
		}

		if( this._y_axis_colour.length > 0 )
			tmp.push(this.format_output('y_axis_colour',this._y_axis_colour));

		if( this._y_grid_colour.length > 0 )
			tmp.push(this.format_output('y_grid_colour',this._y_grid_colour));
  
		if( this.y2_axis_colour.length > 0 )
			tmp.push(this.format_output('y2_axis_colour',this.y2_axis_colour));
		
		if( this.x_offset.length > 0 ) {
			tmp.push(this.format_output('x_offset',this.x_offset));
		}

		if( this.inner_bg_colour.length > 0 ){
			var values = this.inner_bg_colour;
			if( this.inner_bg_colour_2.length > 0 ) {
				values += ','+ this.inner_bg_colour_2;
				values += ','+ this.inner_bg_angle;
			}
			tmp.push(this.format_output('inner_background',values));
		}
	
		if( this._pie.length > 0 ) {
			tmp.push(this.format_output('pie',this._pie));
			tmp.push(this.format_output('links',this._pie_links));
			tmp.push(this.format_output('values',this._pie_values));
			tmp.push(this.format_output('pie_labels',this._pie_labels));
			tmp.push(this.format_output('colours',this._pie_colours));
		}

		if( this.tool_tip.length > 0 )
			tmp.push(this.format_output('tool_tip',this.tool_tip));

		if( this.y_format.length > 0 )
			tmp.push(this.format_output('y_format',this.y_format));
			
		if( this.num_decimals.length > 0 )
			tmp.push(this.format_output('num_decimals',this.num_decimals));
			
		if( this.is_fixed_num_decimals_forced.length > 0 )
			tmp.push(this.format_output('is_fixed_num_decimals_forced',this.is_fixed_num_decimals_forced));
			
		if( this.is_decimal_separator_comma.length > 0 )
			tmp.push(this.format_output('is_decimal_separator_comma',this.is_decimal_separator_comma));
			
		if( this.is_thousand_separator_disabled.length > 0 )
			tmp.push(this.format_output('is_thousand_separator_disabled',this.is_thousand_separator_disabled));


		var count = 1;
		for(var i = 0; i < this.data_sets.length; i++) {
			var set = this.data_sets[i];
			tmp.push(set._toString(this, (count>1) ? '_'+count : ''));
			count++;
		}
		if(this.output_type == 'js') {
			this.so.write(this.unique_id);
			return '';
		} else {
			var query = '';
			for(var i = 0; i < tmp.length ; i++) 
				query += '&'+tmp[i][0] +'='+encodeURIComponent(tmp[i][1]);
			return query;
		}
	}
}

if(typeof line == "undefined") var line = new Object();

/**
 * @class line Object
 * @constructor 
 * @param {int}  line_width The width of line
 * @param {string} colour The hex colour value of the line
 */
function line(line_width, colour ) {
	this.vartype = 'line';
	this.line_width = line_width;
	this.colour = colour;
	this.data = [];
	this.links = [];
	this.tips = [];
	this._key = false;
}

line.prototype = {

	/**
	 * The type of chart.
	 *
	 * @type string
	 */
	vartype:null,

	/**
	 * The width of line.
	 *
	 * @type int
	 */
	line_width:null,

	/**
	 * The hex colour value of the bar.
	 *
	 * @type string
	 */
	colour:null,

	/**
	 * This line has key or not.
	 *
	 * @type boolean
	 */
	_key:false,

	/**
	 * The value of key.
	 *
	 * @type string
	 */
	_vkey:null,

	/**
	 * The font size of key.
	 *
	 * @type int
	 */
	_key_size:null,

	/**
	 * hold the data.
	 *
	 * @type array
	 */
	data:[],

	/**
	 * hold the data.
	 *
	 * @type array
	 */
	links : [],

	/**
	 * extra tool tip info.
	 *
	 * @type array
	 */
	tips:[],

	/**
	 * Set the key and size.
	 *
	 * @param {string} key The text legend values of line.
	 * @param {string} size The font size of text.
	 */
	key : function( key, size ){
		this._key = true;
		this._vkey = graph.prototype.esc( key );
		this._key_size = size;
	},
	
	/**
	 * add  Data to line.
	 *
	 * @param {int} data The data of line.
	 */
	add : function( data ){
		this.data.push(data);
	},
	
	/**
	 * add  data and link to line.
	 *
	 * @param {int} data The data of line.
	 * @param {string} link The link of line.
	 */
	add_link : function( data, link ){
		this.data.push(data);
		this.links.push(graph.prototype.esc( link ));
	},
	
	/**
	 * add  data and tip to line.
	 *
	 * @param {int} data The data of line.
	 * @param {string} tip The tip of line.
	 */
	add_data_tip : function( data, tip )	{
		this.data.push(data);
		this.tips.push(graph.prototype.esc( tip ));
	},
	
	/**
	 * add  data and link and  tip to line.
	 *
	 * @param {int} data The data of line.
	 * @param {string} tip The link of line.
	 */
	add_data_link_tip : function( data, link, tip )	{
		this.data.push(data);
		this.links.push(graph.prototype.esc( link ));
		this.tips.push(graph.prototype.esc( tip ));
	},
	
	/**
	 * return the variables for this chart.
	 *
	 * @return {array} the variables for this chart.
	 */
	_get_variable_list : function() {
		var values = [];
		values.push(this.line_width);
		values.push(this.colour);
		if( this._key )	{
			values.push(this._vkey);
			values.push(this._key_size);
		}
		return values;
	},

	/**
	 * return the string for this chart.
	 *
	 * @param {graph} parentNode  the parent graph object.
	 * @param {int} set_num the number of line.
	 * @return {string} the string for this chart.
	 */
	_toString : function(parentNode, set_num) {
		var values = this._get_variable_list().join(',');
		var tmp = [];
		tmp.push(parentNode.format_output( this.vartype + set_num, values ));
		tmp.push(parentNode.format_output( 'values' + set_num, this.data.join(',') ));
		
		if( this.links.length > 0 )
			tmp.push(parentNode.format_output( 'links' + set_num, this.links.join(',') ));
				
		if( this.tips.length > 0 )
			tmp.push(parentNode.format_output( 'tool_tips_set' + set_num, this.tips.join(',') ));
		return tmp.join('\r\n');
	},

	/**
	 * the alias for _toString.
	 *
	 * @param {graph} parentNode  the parent graph object.
	 * @param {int} set_num the number of line.
	 * @return {string} the string for this chart.
	 */
	toString : function( parentNode, set_num ){
		return this._toString(parentNode, set_num);
	}
}

if(typeof line_hollow == "undefined") var line_hollow = new Object();

/**
 * @class line_hollow Object
 * @constructor 
 * @extends line
 * @param {int}  line_width The width of line
 * @param {int}  dot_size The size of dot
 * @param {string} colour The hex colour value of the line
 */
function line_hollow( line_width, dot_size, colour )	{
	Object.setParent(this, new line(line_width, colour));
	this.vartype = 'line_hollow';
	this.dot_size = dot_size;
}


line_hollow.prototype = {

	/**
	 * The size of dot.
	 *
	 * @type int
	 */
	dot_size :  null,

	/**
	 * return the variables for this chart.
	 *
	 * @return {array} the variables for this chart.
	 */
	_get_variable_list  : function () {
		var values = [];
		values.push(this.line_width);
		values.push(this.colour);
		
		if( this._key ){
			values.push(this.key);
			values.push(this._key_size);
		} else {
			values.push('');
			values.push('');
		}
		values.push(this.dot_size);
		return values;
	}
}


if(typeof line_dot == "undefined") var line_dot = new Object();

/**
 * @class line_dot Object
 * @constructor 
 * @extends line_hollow
 * @param {int}  line_width The width of line
 * @param {int}  dot_size The size of dot
 * @param {string} colour The hex colour value of the line
 */
function line_dot( line_width, dot_size, colour ){
	Object.setParent(this, new line_hollow(line_width, dot_size, colour));
	this.vartype = 'line_dot';
}

if(typeof bar == "undefined") var bar = new Object();

/**
 * @class bar Object
 * @constructor 
 * @param {int} alpha The percentage of transparency of the bar colour.
 * @param {string} colour The hex colour value of the bar
 */
function bar( alpha, colour )	{
	this.vartype = 'bar';
	this.alpha = alpha;
	this.colour = colour;
	this.data = [];
	this.links = [];
	this.tips = [];
	this._key = false;
}

bar.prototype = {

	/**
	 * The hex colour value of the bar.
	 *
	 * @type string
	 */
	colour:null,

	/**
	 * The percentage of transparency of the bar colour.
	 *
	 * @type int
	 */
	alpha:null,

	/**
	 * The values of the bar.
	 *
	 * @type array
	 */
	data:null,

	/**
	 * The links of the bar.
	 *
	 * @type array
	 */
	links:null,

	/**
	 * This line has key or not.
	 *
	 * @type boolean
	 */
	_key:null,

	/**
	 * This  key of line.
	 *
	 * @type string
	 */
	_vkey:null,

	/**
	 * This  key font size of line.
	 *
	 * @type int
	 */
	_key_size:null,

	/**
	 * This type of line.
	 *
	 * @type string
	 */
	vartype:null,

	/**
	 * The tips of the bar.
	 *
	 * @type array
	 */
	tips:null,

	/**
	 * Set the key and size.
	 *
	 * @param {string} key The text legend values of line.
	 * @param {string} size The font size of text.
	 */
	key : function( key, size ){
		this._key = true;
		this._vkey = graph.prototype.esc( key );
		this._key_size = size;
	},
	
	/**
	 * add  Data to line.
	 *
	 * @param {int} data The data of line.
	 */
	add : function( data ){
		this.data.push(data);
	},

	/**
	 * add  data and link to line.
	 *
	 * @param {int} data The data of line.
	 * @param {string} link The link of line.
	 */
	add_link : function( data, link ){
		this.data.push(data);
		this.Links.push(graph.prototype.esc( link ));
	},
	
	/**
	 * add  data and tip to line.
	 *
	 * @param {int} data The data of line.
	 * @param {string} tip The tip of line.
	 */
	add_data_tip : function( data, tip ){
		this.data.push(data);
		this.tips.push(graph.prototype.esc( tip ));
	},
	
	/**
	 * return the variables for this chart.
	 *
	 * @return {array} the variables for this chart.
	 */
	_get_variable_list : function(){
		var values = [];
		values.push(this.alpha);
		values.push(this.colour);
		if( this._key )	{
			values.push(this._vkey);
			values.push(this._key_size);
		}
		return values;
	},
	
	/**
	 * return the string for this chart.
	 *
	 * @param {graph} parentNode  the parent graph object.
	 * @param {int} set_num the number of line.
	 * @return {string} the string for this chart.
	 */
	_toString : function(parentNode, set_num ){
		var values = this._get_variable_list().join(',');
		var tmp = [];
		tmp.push(parentNode.format_output( this.vartype + set_num, values ));
		tmp.push(parentNode.format_output( 'values' + set_num, this.data.join(',') ));
		if( this.links.length > 0 )
			tmp.push(parentNode.format_output( 'links' + set_num, this.links.join(',') ));
		if( this.tips.length > 0 )
			tmp.push(parentNode.format_output( 'tool_tips_set' + set_num, this.tips.join(',') ));
		return tmp.join('\r\n');
	},

	/**
	 * the alias for _toString.
	 *
	 * @param {graph} parentNode  the parent graph object.
	 * @param {int} set_num the number of line.
	 * @return {string} the string for this chart.
	 */
	toString : function(parentNode, set_num ){
		return this._toString(parentNode, set_num);
	}
}


if(typeof bar_3d == "undefined") var bar_3d = new Object();

/**
 * @class bar_3d Object
 * @constructor 
 * @extends bar
 * @param {int} alpha The percentage of transparency of the bar colour.
 * @param {string} colour The hex colour value of the bar
 */
function bar_3d( alpha, colour){
	Object.setParent(this, new bar( alpha, colour));
	this.vartype = 'bar_3d';
}

if(typeof bar_fade == "undefined") var bar_fade = new Object();

/**
 * @class bar_fade Object
 * @constructor 
 * @extends bar
 * @param {int} alpha The percentage of transparency of the bar colour.
 * @param {string} colour The hex colour value of the bar
 */
function bar_fade( alpha, colour){
	Object.setParent(this, new bar( alpha, colour));
	this.vartype = 'bar_fade';
}


if(typeof bar_outline == "undefined") var bar_outline = new Object();

/**
 * @class bar_outline Object
 * @constructor 
 * @extends bar
 * @param {int} alpha The percentage of transparency of the bar colour.
 * @param {string} colour The hex colour value of the bar
 * @param {string} outline_colour The hex colour value of the bar outline
 */
function bar_outline( alpha, colour, outline_colour ) 	{
	Object.setParent(this, new bar( alpha, colour));
	this.vartype = 'filled_bar';
	this.outline_colour = outline_colour;
}

bar_outline.prototype = {

	/**
	 * The hex colour value of the bar outline.
	 *
	 * @type string
	 */
	outline_colour : null,

	/**
	 * return the variables for this chart.
	 * override the base method
	 *
	 * @return {array} the variables for this chart.
	 */
	_get_variable_list :function (){
		var values = [];
		values.push(this.alpha);
		values.push(this.colour);
		values.push(this.outline_colour);
		if( this._key )	{
			values.push(this._vkey);
			values.push(this._key_size);
		}
		return values;
	}
}

if(typeof bar_glass == "undefined") var bar_glass = new Object();

/**
 * @class bar_glass Object
 * @constructor 
 * @extends bar_outline
 * @param {int} alpha The percentage of transparency of the bar colour.
 * @param {string} colour The hex colour value of the bar
 * @param {string} outline_colour The hex colour value of the bar outline
 */
function bar_glass( alpha, colour, outline_colour ) 	{
	Object.setParent(this, new bar_outline( alpha, colour, outline_colour));
	this.vartype = 'bar_glass';
}

if(typeof bar_sketch == "undefined") var bar_sketch = new Object();

/**
 * @class bar_sketch Object
 * @constructor 
 * @extends bar_outline
 * @param {int} alpha The percentage of transparency of the bar colour.
 * @param {int} offset The offset of line of the bar colour.
 * @param {string} colour The hex colour value of the bar
 * @param {string} outline_colour The hex colour value of the bar outline
 */
function bar_sketch( alpha, offset, colour, outline_colour ){
	Object.setParent(this, new bar_outline( alpha, colour, outline_colour));
	this.vartype = 'bar_sketch';
	this.offset = offset;
}

bar_sketch.prototype = {

	/**
	 * The offset of line.
	 *
	 * @type int
	 */
	offset : null,

	/**
	 * return the variables for this chart.
	 * override the base method
	 *
	 * @return {array} the variables for this chart.
	 */
	_get_variable_list : function(){
		var values = [];
		values.push(this.alpha);
		values.push(this.offset);
		values.push(this.colour);
		values.push(this.outline_colour);
		if( this._key ){
			values.push(this._vkey);
			values.push(this._key_size);
		}
		return values;
	}
}

if(typeof candle == "undefined") var candle = new Object();

/**
 * @class candle Object
 * @constructor 
 * @param {int} high The high value of the point.
 * @param {int} open The open value of the point.
 * @param {int} close The close value of the point.
 * @param {int} low The low value of the point.
 */
function candle( high, open, close, low ){
	this.out = [];
	this.out.push(high);
	this.out.push(open);
	this.out.push(close);
	this.out.push(low);
}

candle.prototype = {

	/**
	 * Point data information as array be saved like [high,open,close,low].
	 *
	 * @type array
	 */
	out: null,

	/**
	 * return the string for this point.
	 *
	 * @return {string} the string for this point.
	 */
	_toString : function() {
		return '['+ this.out.join(',')+']';
	},

	/**
	 * The alias of _toString.
	 *
	 * @return {string} the string for this point.
	 */
	toString : function() {
		return this._toString();
	}
}

if(typeof hlc == "undefined") var hlc = new Object();

/**
 * @class hlc Object
 * @constructor 
 * @param {int} high The high value of the point.
 * @param {int} low The low value of the point.
 * @param {int} close The close value of the point.
 */
function hlc( high, low, close ){
	this.out = [];
	this.out.push(high);
	this.out.push(low);
	this.out.push(close);
}

hlc.prototype = {

	/**
	 * Point data information as array be saved like [high,low, close].
	 *
	 * @type array
	 */
	out : null,

	/**
	 * return the string for this point.
	 *
	 * @return {string} the string for this point.
	 */
	_toString : function(){
		return '['+ this.out.join(',') +']';
	},

	/**
	 * The alias of _toString.
	 *
	 * @return {string} the string for this point.
	 */
	toString : function(){
		return this._toString();
	}
}

if(typeof point == "undefined") var point = new Object();

/**
 * @class point Object
 * @constructor 
 * @param {int} x The x value of point.
 * @param {int} y The y value of point.
 * @param {int} size_px The size of point circle.
 */
function point( x, y, size_px ){
	this.out = [];
	this.out.push(x);
	this.out.push(y);
	this.out.push(size_px);
}

point.prototype = {

	/**
	 * Point data information as array be saved like [x,y, size].
	 *
	 * @type array
	 */
	out : null,

	/**
	 * return the string for this point.
	 *
	 * @return {string} the string for this point.
	 */
	_toString : function() {
		return '['+ this.out.join(',') +']';
	},

	/**
	 * The alias of _toString.
	 *
	 * @return {string} the string for this point.
	 */
	toString : function() {
		return '['+ this.out.join(',') +']';
	}
}




