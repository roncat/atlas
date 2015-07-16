'use strict';

var gulp = require('gulp');
var config       = require('../config');

gulp.task('assets', function() {

	return gulp.src(config.assets.src)	
	.pipe(gulp.dest(config.assets.dest));
	   
});