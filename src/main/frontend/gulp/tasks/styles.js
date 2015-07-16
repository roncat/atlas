
'use strict';

var config       = require('../config');
var gulp         = require('gulp');
var sass         = require('gulp-sass');
var less         = require('gulp-less');
var sourcemaps   = require('gulp-sourcemaps');
var gulpif       = require('gulp-if');
var handleErrors = require('../util/handleErrors');
var browserSync  = require('browser-sync');
var autoprefixer = require('gulp-autoprefixer');
var concat       = require('gulp-concat');
var minifyCss    = require('gulp-minify-css');
var path         = require('path');

gulp.task('styles', function () {

return gulp.src(config.styles.src)	
.pipe(sourcemaps.init())
.pipe(less())
.pipe(autoprefixer())
.pipe(concat('main.css'))
.pipe(minifyCss())
.pipe(sourcemaps.write('./maps'))
.pipe(gulp.dest(config.styles.dest))
   .pipe(gulpif(browserSync.active, browserSync.reload({ stream: true })));


});