<%= _.classify(appname) %>.User = DS.Model.extend({
    name: DS.attr('string'),
    email: DS.attr('string')
});
