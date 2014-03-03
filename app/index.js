'use strict';
var util = require('util'),
    path = require('path'),
    yeoman = require('yeoman-generator'),
    chalk = require('chalk'),
    _s = require('underscore.string');

var JhipsterGenerator = module.exports = function JhipsterGenerator(args, options, config) {
    yeoman.generators.Base.apply(this, arguments);

    this.on('end', function () {
        this.installDependencies({
            skipInstall: options['skip-install'],
            callback: function() {
                this.spawnCommand('gradle', ['wrapper']);
            }.bind(this)
        });
    });
    this.pkg = JSON.parse(this.readFileAsString(path.join(__dirname, '../package.json')));
};

util.inherits(JhipsterGenerator, yeoman.generators.Base);

JhipsterGenerator.prototype.askFor = function askFor() {
    var cb = this.async();

    console.log(chalk.red('\n' +
        ' _     _   ___   __  _____  ____  ___       __  _____   __    __    _    \n' +
        '| |_| | | | |_) ( (`  | |  | |_  | |_)     ( (`  | |   / /\\  / /`  | |_/ \n' +
        '|_| | |_| |_|   _)_)  |_|  |_|__ |_| \\     _)_)  |_|  /_/--\\ \\_\\_, |_| \\ \n' +
        '                             ____  ___   ___                             \n' +
        '                            | |_  / / \\ | |_)                            \n' +
        '                            |_|   \\_\\_/ |_| \\                            \n' +
        '              _    __    _       __        ___   ____  _      __        \n' +
        '             | |  / /\\  \\ \\  /  / /\\      | | \\ | |_  \\ \\  / ( (`       \n' +
        '           \\_|_| /_/--\\  \\_\\/  /_/--\\     |_|_/ |_|__  \\_\\/  _)_)       \n' +
        '\n'));

    console.log('\nWelcome to the Jhipster NG Generator\n\n');

    var prompts = [
        {
            type: 'input',
            name: 'baseName',
            message: '(1/6) What is the base name of your application?',
            default: 'jhipster-ng'
        },
        {
            type: 'input',
            name: 'packageName',
            message: '(2/6) What is your default Java package name?',
            default: 'com.mycompany.myapp'
        },
        {
            type: 'list',
            name: 'hibernateCache',
            message: '(3/6) Do you want to use Hibernate 2nd level cache?',
            choices: [
                {
                    value: 'no',
                    name: 'No'
                },
                {
                    value: 'ehcache',
                    name: 'Yes, with ehcache (local cache, for a single node)'
                },
                {
                    value: 'hazelcast',
                    name: 'Yes, with HazelCast (distributed cache, for multiple nodes)'
                }
            ],
            default: 0
        },
        {
            type: 'list',
            name: 'clusteredHttpSession',
            message: '(4/6) Do you want to use clustered HTTP sessions?',
            choices: [
                {
                    value: 'no',
                    name: 'No'
                },
                {
                    value: 'hazelcast',
                    name: 'Yes, with HazelCast'
                }
            ],
            default: 0
        },
        {
            type: 'list',
            name: 'prodDatabaseType',
            message: '(5/6) Which *production* database would you like to use?',
            choices: [
                {
                    value: 'mysql',
                    name: 'MySQL'
                },
                {
                    value: 'postgresql',
                    name: 'PostgreSQL'
                }
            ],
            default: 1
        },
        {
            type: 'list',
            name: 'devDatabaseType',
            message: '(6/6) Which *development* database would you like to use?',
            choices: [
                {
                    value: 'h2Memory',
                    name: 'H2 in-memory'
                },
                {
                    value: 'mysql',
                    name: 'MySQL'
                },
                {
                    value: 'postgresql',
                    name: 'PostgreSQL'
                }
            ],
            default: 0
        }
    ];

    this.prompt(prompts, function (props) {
        this.springVersion = props.springVersion;
        this.springSecurityVersion = props.springSecurityVersion;
        this.packageName = props.packageName;
        this.baseName = props.baseName;
        this.hibernateCache = props.hibernateCache;
        this.clusteredHttpSession = props.clusteredHttpSession;
        this.devDatabaseType = props.devDatabaseType;
        this.prodDatabaseType = props.prodDatabaseType;

        cb();
    }.bind(this));
};

JhipsterGenerator.prototype.app = function app() {

    this.template('_package.json', 'package.json');
    this.template('_bower.json', 'bower.json');
    this.template('_README.md', 'README.md');
    this.template('bowerrc', '.bowerrc');
    this.copy('Gruntfile.js', 'Gruntfile.js');
    this.copy('gitignore', '.gitignore');
    this.copy('spring_loaded/springloaded-1.2.0-dev.jar', 'spring_loaded/springloaded-1.2.0-dev.jar');

    var packageFolder = this.packageName.replace(/\./g, '/');
    this.template('_build.gradle', 'build.gradle');

    // Create Java resource files
    var resourceDir = 'src/main/resources/';
    this.mkdir(resourceDir);

    if (this.hibernateCache == "ehcache") {
        this.template(resourceDir + '_ehcache.xml', resourceDir + 'ehcache.xml');
    }

    // i18n resources used by thymeleaf
    this.copy(resourceDir + '/i18n/messages_en.properties', resourceDir + 'i18n/messages_en.properties');
    this.copy(resourceDir + '/i18n/messages_fr.properties', resourceDir + 'i18n/messages_fr.properties');
    this.copy(resourceDir + '/i18n/messages_de.properties', resourceDir + 'i18n/messages_de.properties');

    // Thymeleaf templates
    this.copy(resourceDir + '/templates/error.html', resourceDir + 'templates/error.html');

    this.template(resourceDir + '_logback.xml', resourceDir + 'logback.xml');

    this.template(resourceDir + '/config/_application.yml', resourceDir + 'config/application.yml');
    this.template(resourceDir + '/config/_application-dev.yml', resourceDir + 'config/application-dev.yml');
    this.template(resourceDir + '/config/_application-prod.yml', resourceDir + 'config/application-prod.yml');

    this.copy(resourceDir + '/config/liquibase/db-changelog.xml', resourceDir + 'config/liquibase/db-changelog.xml');
    this.copy(resourceDir + '/config/liquibase/users.csv', resourceDir + 'config/liquibase/users.csv');
    this.copy(resourceDir + '/config/liquibase/authorities.csv', resourceDir + 'config/liquibase/authorities.csv');
    this.copy(resourceDir + '/config/liquibase/users_authorities.csv', resourceDir + 'config/liquibase/users_authorities.csv');

    // Create Java files
    var javaDir = 'src/main/java/' + packageFolder + '/';

    this.template('src/main/java/package/_Application.java', javaDir + '/Application.java');

    this.template('src/main/java/package/config/_package-info.java', javaDir + 'config/package-info.java');
    this.template('src/main/java/package/config/_AsyncConfiguration.java', javaDir + 'config/AsyncConfiguration.java');
    this.template('src/main/java/package/config/_CacheConfiguration.java', javaDir + 'config/CacheConfiguration.java');
    this.template('src/main/java/package/config/_Constants.java', javaDir + 'config/Constants.java');
    this.template('src/main/java/package/config/_DatabaseConfiguration.java', javaDir + 'config/DatabaseConfiguration.java');
    this.template('src/main/java/package/config/_LocaleConfiguration.java', javaDir + 'config/LocaleConfiguration.java');
    this.template('src/main/java/package/config/_MailConfiguration.java', javaDir + 'config/MailConfiguration.java');
    this.template('src/main/java/package/config/_MetricsConfiguration.java', javaDir + 'config/MetricsConfiguration.java');
    this.template('src/main/java/package/config/_ThymeleafConfiguration.java', javaDir + 'config/ThymeleafConfiguration.java');
    this.template('src/main/java/package/config/_WebConfigurer.java', javaDir + 'config/WebConfigurer.java');
    this.template('src/main/java/package/config/_SecurityConfiguration.java', javaDir + 'config/SecurityConfiguration.java');
    this.template('src/main/java/package/config/_OAuth2ServerConfig.java', javaDir + 'config/OAuth2ServerConfig.java');

    this.template('src/main/java/package/config/audit/_package-info.java', javaDir + 'config/audit/package-info.java');
    this.template('src/main/java/package/config/audit/_AuditEventConverter.java', javaDir + 'config/audit/AuditEventConverter.java');

    this.template('src/main/java/package/config/locale/_package-info.java', javaDir + 'config/locale/package-info.java');
    this.template('src/main/java/package/config/locale/_AngularCookieLocaleResolver.java', javaDir + 'config/locale/AngularCookieLocaleResolver.java');

    this.template('src/main/java/package/config/metrics/_package-info.java', javaDir + 'config/metrics/package-info.java');
    this.template('src/main/java/package/config/metrics/_DatabaseHealthCheck.java', javaDir + 'config/metrics/DatabaseHealthCheck.java');
    this.template('src/main/java/package/config/metrics/_JavaMailHealthCheck.java', javaDir + 'config/metrics/JavaMailHealthCheck.java');

    if (this.hibernateCache == "hazelcast") {
        this.template('src/main/java/package/config/hazelcast/_HazelcastCacheRegionFactory.java', javaDir + 'config/hazelcast/HazelcastCacheRegionFactory.java');
        this.template('src/main/java/package/config/hazelcast/_package-info.java', javaDir + 'config/hazelcast/package-info.java');
    }

    this.template('src/main/java/package/config/reload/_package-info.java', javaDir + 'config/reload/package-info.java');
    this.template('src/main/java/package/config/reload/_JHipsterFileSystemWatcher.java', javaDir + 'config/reload/JHipsterFileSystemWatcher.java');
    this.template('src/main/java/package/config/reload/_JHipsterPluginManagerReloadPlugin.java', javaDir + 'config/reload/JHipsterPluginManagerReloadPlugin.java');
    this.template('src/main/java/package/config/reload/_SpringReloader.java', javaDir + 'config/reload/SpringReloader.java');
    this.template('src/main/java/package/config/reload/_JacksonReloader.java', javaDir + 'config/reload/JacksonReloader.java');

    this.template('src/main/java/package/domain/_package-info.java', javaDir + 'domain/package-info.java');
    this.template('src/main/java/package/domain/_Authority.java', javaDir + 'domain/Authority.java');
    this.template('src/main/java/package/domain/_PersistentAuditEvent.java', javaDir + 'domain/PersistentAuditEvent.java');
    this.template('src/main/java/package/domain/_User.java', javaDir + 'domain/User.java');
    this.template('src/main/java/package/domain/_Base.java', javaDir + 'domain/Base.java');
    this.template('src/main/java/package/domain/util/_CustomLocalDateSerializer.java', javaDir + 'domain/util/CustomLocalDateSerializer.java');

    this.template('src/main/java/package/repository/_package-info.java', javaDir + 'repository/package-info.java');
    this.template('src/main/java/package/repository/_AuthorityRepository.java', javaDir + 'repository/AuthorityRepository.java');
    this.template('src/main/java/package/repository/_CustomAuditEventRepository.java', javaDir + 'repository/CustomAuditEventRepository.java');
    this.template('src/main/java/package/repository/_UserRepository.java', javaDir + 'repository/UserRepository.java');
    this.template('src/main/java/package/repository/_PersistenceAuditEventRepository.java', javaDir + 'repository/PersistenceAuditEventRepository.java');

    this.template('src/main/java/package/security/_package-info.java', javaDir + 'security/package-info.java');
    this.template('src/main/java/package/security/_AuthoritiesConstants.java', javaDir + 'security/AuthoritiesConstants.java');
    this.template('src/main/java/package/security/_SecurityUtils.java', javaDir + 'security/SecurityUtils.java');
    this.template('src/main/java/package/security/_UserApprovalHandler.java', javaDir + 'security/UserApprovalHandler.java');

    this.template('src/main/java/package/service/_package-info.java', javaDir + 'service/package-info.java');
    this.template('src/main/java/package/service/_MailService.java', javaDir + 'service/MailService.java');
        this.template('src/main/java/package/service/_AuditEventService.java', javaDir + 'service/AuditEventService.java');

    this.template('src/main/java/package/web/filter/_package-info.java', javaDir + 'web/filter/package-info.java');
    this.template('src/main/java/package/web/filter/_CachingHttpHeadersFilter.java', javaDir + 'web/filter/CachingHttpHeadersFilter.java');
    this.template('src/main/java/package/web/filter/_StaticResourcesProductionFilter.java', javaDir + 'web/filter/StaticResourcesProductionFilter.java');

    this.template('src/main/java/package/web/filter/gzip/_package-info.java', javaDir + 'web/filter/gzip/package-info.java');
    this.template('src/main/java/package/web/filter/gzip/_GzipResponseHeadersNotModifiableException.java', javaDir + 'web/filter/gzip/GzipResponseHeadersNotModifiableException.java');
    this.template('src/main/java/package/web/filter/gzip/_GZipResponseUtil.java', javaDir + 'web/filter/gzip/GZipResponseUtil.java');
    this.template('src/main/java/package/web/filter/gzip/_GZipServletFilter.java', javaDir + 'web/filter/gzip/GZipServletFilter.java');
    this.template('src/main/java/package/web/filter/gzip/_GZipServletOutputStream.java', javaDir + 'web/filter/gzip/GZipServletOutputStream.java');
    this.template('src/main/java/package/web/filter/gzip/_GZipServletResponseWrapper.java', javaDir + 'web/filter/gzip/GZipServletResponseWrapper.java');

    this.template('src/main/java/package/web/propertyeditors/_package-info.java', javaDir + 'web/propertyeditors/package-info.java');
    this.template('src/main/java/package/web/propertyeditors/_LocaleDateTimeEditor.java', javaDir + 'web/propertyeditors/LocaleDateTimeEditor.java');

    this.template('src/main/java/package/web/rest/dto/_package-info.java', javaDir + 'web/rest/dto/package-info.java');
    this.template('src/main/java/package/web/rest/dto/_LoggerDTO.java', javaDir + 'web/rest/dto/LoggerDTO.java');
    this.template('src/main/java/package/web/rest/dto/_UserDTO.java', javaDir + 'web/rest/dto/UserDTO.java');
    this.template('src/main/java/package/web/rest/_package-info.java', javaDir + 'web/rest/package-info.java');
    this.template('src/main/java/package/web/rest/_AuditResource.java', javaDir + 'web/rest/AuditResource.java');
    this.template('src/main/java/package/web/rest/_LogsResource.java', javaDir + 'web/rest/LogsResource.java');

    this.template('src/main/java/package/web/servlet/_package-info.java', javaDir + 'web/servlet/package-info.java');
    this.template('src/main/java/package/web/servlet/_HealthCheckServlet.java', javaDir + 'web/servlet/HealthCheckServlet.java');

    // Create Test Java files
    var testDir = 'src/test/java/' + packageFolder + '/';
    var testResourceDir = 'src/test/resources/';
    this.mkdir(testDir);

    //this.template(testResourceDir + 'config/_application.yml', testResourceDir + 'config/application.yml');
    //this.template(testResourceDir + '_logback.xml', testResourceDir + 'logback.xml');

    //if (this.hibernateCache == "ehcache") {
    //    this.template(testResourceDir + '_ehcache.xml', testResourceDir + 'ehcache.xml');
    //}

    // Create Webapp
    var webappDir = 'src/main/resources/public/';
    this.mkdir(webappDir);

    this.copy(webappDir + 'styles/main.less', webappDir + 'styles/main.less');
    this.copy(webappDir + 'fonts/glyphicons-halflings-regular.eot', webappDir + 'fonts/glyphicons-halflings-regular.eot');
    this.copy(webappDir + 'fonts/glyphicons-halflings-regular.svg', webappDir + 'fonts/glyphicons-halflings-regular.svg');
    this.copy(webappDir + 'fonts/glyphicons-halflings-regular.ttf', webappDir + 'fonts/glyphicons-halflings-regular.ttf');
    this.copy(webappDir + 'fonts/glyphicons-halflings-regular.woff', webappDir + 'fonts/glyphicons-halflings-regular.woff');

    // HTML5 BoilerPlate
    this.copy(webappDir + 'favicon.ico', webappDir + 'favicon.ico');
    this.copy(webappDir + 'robots.txt', webappDir + 'robots.txt');
    this.copy(webappDir + 'htaccess.txt', webappDir + '.htaccess');

    // i18n
    this.template(webappDir + '/i18n/_en.json', webappDir + 'i18n/en.json');
    this.template(webappDir + '/i18n/_fr.json', webappDir + 'i18n/fr.json');
    this.template(webappDir + '/i18n/_de.json', webappDir + 'i18n/de.json');

    // Index page
    this.indexFile = this.readFileAsString(path.join(this.sourceRoot(), webappDir + '_index.html'));
    this.indexFile = this.engine(this.indexFile, this);

    // JavaScript
    this.template(webappDir + 'scripts/_app.js', webappDir + 'scripts/app.js');
    this.template(webappDir + 'scripts/_router.js', webappDir + 'scripts/router.js');
    this.template(webappDir + 'scripts/_store.js', webappDir + 'scripts/store.js');

    this.copy(webappDir + 'templates/index.hbs', webappDir + 'templates/index.hbs');

    // Create Test Javascript files
    var testJsDir = 'src/test/javascript/';
    this.mkdir(testJsDir);

    this.write(webappDir + 'index.html', this.indexFile);

    this.config.set('baseName', this.baseName);
    this.config.set('packageName', this.packageName);
    this.config.set('packageFolder', packageFolder);
    this.config.set('hibernateCache', this.hibernateCache);
};

JhipsterGenerator.prototype.projectfiles = function projectfiles() {
    this.copy('editorconfig', '.editorconfig');
    this.copy('jshintrc', '.jshintrc');
};
