<%= _.classify(appname) %>.Router.map(function () {
    this.route('login', {
        path: '/login'
    });
    this.route('index', {
        path: '/'
    });
});
