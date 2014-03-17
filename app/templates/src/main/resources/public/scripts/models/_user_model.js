<%= _.classify(appname) %>.User = DS.Model.extend({
    login: DS.attr('string'),
    name: DS.attr('string'),
    email: DS.attr('string')
});
