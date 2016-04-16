module.exports = function (grunt) {

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        concat: {
            options: {
                // define a string to put between each file in the concatenated output
                separator: ';'
            },
            dist: {
                // The files to concatenate
                src: ['src/**/*.js'],
                // The location of the resulting JS file
                dest: 'dist/<%= pkg.name %>.js'
            }
        },

        uglify: {
            options: {
                // the banner that is inserted at the top of the output
                banner: '/*! <%= pkg.name %> <%= grunt.template.today("dd-mm-yyy") %> */\n'
            },
            dist: {
                files: {
                    'dist/<%= pkg.name %>.min.js': ['<%= concat.dist.dest %>']
                }
            }
        },

        qunit: {
            files : ['test/**/*.html']
        },

        jshint: {
            // define the files to line
            files: ['Gruntfile.js', 'src/**/*.js', 'test/**/*.js'],
            // configure JSHint (documeted at http://www.jshint.com/docs/)
            options: {
                // more options here if you want to override JSHint defaults
                globals: {
                    jQuery: true,
                    console: true,
                    module: true
                }
            }
        },

        watch: {
            files : ['<%= jshint.files %>'],
            tasks: ['jshint', 'qunit']
        }
    });

    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-qunit');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-concat');
    
    grunt.registerTask('test', ['jshint', 'qunit']);
    
    grunt.registerTask('default', ['jshint', 'qunit', 'concat', 'uglify']);
};