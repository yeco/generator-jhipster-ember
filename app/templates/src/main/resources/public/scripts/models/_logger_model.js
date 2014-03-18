<%= _.classify(appname) %>.Logger = DS.Model.extend({
    level: DS.attr('string'),
    isTraceLvl: function() {
        return this.get('level') === 'TRACE';
    }.property('level'),
    isDebugLvl: function() {
        return this.get('level') === 'DEBUG';
    }.property('level'),
    isErrorLvl: function() {
        return this.get('level') === 'ERROR';
    }.property('level'),
    isWarnLvl: function() {
        return this.get('level') === 'WARN';
    }.property('level'),
    isInfoLvl: function() {
        return this.get('level') === 'INFO';
    }.property('level')
});
