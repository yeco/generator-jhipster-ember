<%= _.classify(appname) %>.Router.map(function () {
    this.route('login', {
        path: '/login'
    });
    this.route('index', {
        path: '/'
    });
    this.route('logs_config', {
        path: '/loggers'
    });
    this.route('audit_event'), {
        path: '/auditEvents'
    }
});
