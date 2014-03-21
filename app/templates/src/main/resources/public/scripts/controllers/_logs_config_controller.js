<%= _.classify(appname) %>.LogsConfigController = Ember.ArrayController.extend({
    actions: {
        update: function (logger, level) {
            logger.set('level', level);
            logger.save();
        },
        nextPage: function () {
            if (!this.get('isLastPage')) {
                this.goToPage(this.get('meta')['number'] + 1);
            }
        },
        previousPage: function () {
            if (!this.get('isFirstPage')) {
                this.goToPage(this.get('meta')['number'] - 1);
            }
        }
    },
    goToPage: function (pageNumber) {
        var self = this;
        this.get('store').find('logger', {page: pageNumber}).then(function (model) {
            self.set('model', model);
            self.set('meta', Ember.copy(self.get('store').metadataFor('logger')));
        });
    },
    currentPage: function () {
        if (typeof this.get('meta') !== 'undefined') {
            return this.get('meta')['number'] + 1;
        }
    }.property('meta.number'),
    totalPages: function () {
        if (typeof this.get('meta') !== 'undefined') {
            return this.get('meta').totalPages;
        }
    }.property('meta.totalPages'),
    isFirstPage: function () {
        if (typeof this.get('meta') !== 'undefined') {
            return this.get('meta').firstPage;
        }
    }.property('meta.firstPage'),
    isLastPage: function () {
        if (typeof this.get('meta') !== 'undefined') {
            return this.get('meta').lastPage;
        }
    }.property('meta.lastPage')
});
