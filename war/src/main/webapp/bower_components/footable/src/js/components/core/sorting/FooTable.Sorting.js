(function ($, F) {
	F.Sorting = F.Component.extend(/** @lends FooTable.Sorting */{
		/**
		 * The sorting component adds a small sort button to specified column headers allowing users to sort those columns in the table.
		 * @constructs
		 * @extends FooTable.Component
		 * @param {FooTable.Table} table - The parent {@link FooTable.Table} object for the component.
		 * @returns {FooTable.Sorting}
		 */
		construct: function (table) {
			// call the constructor of the base class
			this._super(table, table.o.sorting.enabled);

			/* PROTECTED */
			/**
			 * This provides a shortcut to the {@link FooTable.Table#options}.[sorting]{@link FooTable.Defaults#sorting} object.
			 * @instance
			 * @protected
			 * @type {object}
			 */
			this.o = table.o.sorting;
			/**
			 * The current sorted column.
			 * @instance
			 * @type {FooTable.Column}
			 */
			this.column = null;

			/* PRIVATE */
			/**
			 * Sets a flag indicating whether or not the sorting has changed. When set to true the {@link FooTable.Sorting#sorting_changing} and {@link FooTable.Sorting#sorting_changed} events
			 * will be raised during the drawing operation.
			 * @private
			 * @type {boolean}
			 */
			this._changed = false;
		},

		/* PROTECTED */
		/**
		 * Checks the supplied data and options for the sorting component.
		 * @instance
		 * @protected
		 * @param {object} data - The jQuery data object from the parent table.
		 * @fires FooTable.Sorting#"preinit.ft.sorting"
		 * @this FooTable.Sorting
		 */
		preinit: function(data){
			var self = this;
			/**
			 * The preinit.ft.sorting event is raised before the UI is created and provides the tables jQuery data object for additional options parsing.
			 * Calling preventDefault on this event will disable the component.
			 * @event FooTable.Sorting#"preinit.ft.sorting"
			 * @param {jQuery.Event} e - The jQuery.Event object for the event.
			 * @param {FooTable.Table} ft - The instance of the plugin raising the event.
			 * @param {object} data - The jQuery data object of the table raising the event.
			 */
			this.ft.raise('preinit.ft.sorting', [data]).then(function(){
				if (self.ft.$el.hasClass('footable-sorting'))
					self.enabled = true;
				self.enabled = F.is.boolean(data.sorting)
					? data.sorting
					: self.enabled;
				if (!self.enabled) return;
				self.column = F.arr.first(self.ft.columns.array, function(col){ return col.sorted; });
			}, function(){
				self.enabled = false;
			});
		},
		/**
		 * Initializes the sorting component for the plugin using the supplied table and options.
		 * @instance
		 * @protected
		 * @fires FooTable.Sorting#"init.ft.sorting"
		 * @this FooTable.Sorting
		 */
		init: function () {
			/**
			 * The init.ft.sorting event is raised before its UI is generated.
			 * Calling preventDefault on this event will disable the component.
			 * @event FooTable.Sorting#"init.ft.sorting"
			 * @param {jQuery.Event} e - The jQuery.Event object for the event.
			 * @param {FooTable.Table} ft - The instance of the plugin raising the event.
			 */
			var self = this;
			this.ft.raise('init.ft.sorting').then(function(){
				F.arr.each(self.ft.columns.array, function(col){
					if (col.sortable){
						col.$el.addClass('footable-sortable').append($('<span/>', {'class': 'fooicon fooicon-sort'}));
					}
				});
				self.ft.$el.on('click.footable', '.footable-sortable', { self: self }, self._onSortClicked);
			}, function(){
				self.enabled = false;
			});
		},
		/**
		 * Destroys the sorting component removing any UI generated from the table.
		 * @instance
		 * @protected
		 * @fires FooTable.Sorting#"destroy.ft.sorting"
		 */
		destroy: function () {
			/**
			 * The destroy.ft.sorting event is raised before its UI is removed.
			 * Calling preventDefault on this event will prevent the component from being destroyed.
			 * @event FooTable.Sorting#"destroy.ft.sorting"
			 * @param {jQuery.Event} e - The jQuery.Event object for the event.
			 * @param {FooTable.Table} ft - The instance of the plugin raising the event.
			 */
			var self = this;
			this.ft.raise('destroy.ft.paging').then(function(){
				self.ft.$el.off('click.footable', '.footable-sortable', self._onSortClicked);
				self.ft.$el.children('thead').children('tr.footable-header')
					.children('.footable-sortable').removeClass('footable-sortable')
					.find('span.fooicon').remove();
			});
		},
		/**
		 * Performs the actual sorting against the {@link FooTable.Rows#current} array.
		 * @instance
		 * @protected
		 */
		predraw: function () {
			if (!this.column) return;
			var self = this, col = self.column;
			//self.ft.rows.array.sort(function (a, b) {
			//	return col.direction == 'ASC'
			//			? col.sorter(a.cells[col.index].value, b.cells[col.index].value)
			//			: col.sorter(b.cells[col.index].value, a.cells[col.index].value);
			//});
			self.ft.rows.array.sort(function (a, b) {
				return col.direction == 'ASC'
						? col.sorter(a.cells[col.index].sortValue, b.cells[col.index].sortValue)
						: col.sorter(b.cells[col.index].sortValue, a.cells[col.index].sortValue);
			});
		},
		/**
		 * Updates the sorting UI setting the state of the sort buttons.
		 * @instance
		 * @protected
		 */
		draw: function () {
			if (!this.column) return;
			var self = this,
				$sortable = self.ft.$el.find('thead > tr > .footable-sortable'),
				$active = self.column.$el;

			$sortable.removeClass('footable-asc footable-desc').children('.fooicon').removeClass('fooicon-sort fooicon-sort-asc fooicon-sort-desc');
			$sortable.not($active).children('.fooicon').addClass('fooicon-sort');
			$active.addClass(self.column.direction == 'ASC' ? 'footable-asc' : 'footable-desc')
				.children('.fooicon').addClass(self.column.direction == 'ASC' ? 'fooicon-sort-asc' : 'fooicon-sort-desc');
		},

		/* PUBLIC */
		/**
		 * Sets the sorting options and calls the {@link FooTable.Table#draw} method to perform the actual sorting.
		 * @instance
		 * @param {(string|number|FooTable.Column)} column - The column name, index or the actual {@link FooTable.Column} object to sort by.
		 * @param {string} [direction="ASC"] - The direction to sort by, either ASC or DESC.
		 * @returns {jQuery.Promise}
		 * @fires FooTable.Sorting#"before.ft.sorting"
		 * @fires FooTable.Sorting#"after.ft.sorting"
		 */
		sort: function(column, direction){
			return this._sort(column, direction);
		},

		/* PRIVATE */
		/**
		 * Performs the required steps to handle sorting including the raising of the {@link FooTable.Sorting#"before.ft.sorting"} and {@link FooTable.Sorting#"after.ft.sorting"} events.
		 * @instance
		 * @private
		 * @param {(string|number|FooTable.Column)} column - The column name, index or the actual {@link FooTable.Column} object to sort by.
		 * @param {string} [direction="ASC"] - The direction to sort by, either ASC or DESC.
		 * @returns {jQuery.Promise}
		 * @fires FooTable.Sorting#"before.ft.sorting"
		 * @fires FooTable.Sorting#"after.ft.sorting"
		 */
		_sort: function(column, direction){
			var self = this;
			var sorter = new F.Sorter(self.ft.columns.get(column), F.Sorting.dir(direction));
			/**
			 * The before.ft.sorting event is raised before a sort is applied and allows listeners to modify the sorter or cancel it completely by calling preventDefault on the jQuery.Event object.
			 * @event FooTable.Sorting#"before.ft.sorting"
			 * @param {jQuery.Event} e - The jQuery.Event object for the event.
			 * @param {FooTable.Table} ft - The instance of the plugin raising the event.
			 * @param {FooTable.Sorter} sorter - The sorter that is about to be applied.
			 */
			return self.ft.raise('before.ft.sorting', [sorter]).then(function(){
				F.arr.each(self.ft.columns.array, function(col){
					if (col != self.column) col.direction = null;
				});
				self.column = self.ft.columns.get(sorter.column);
				if (self.column) self.column.direction = F.Sorting.dir(sorter.direction);
				return self.ft.draw().then(function(){
					/**
					 * The after.ft.sorting event is raised after a sorter has been applied.
					 * @event FooTable.Sorting#"after.ft.sorting"
					 * @param {jQuery.Event} e - The jQuery.Event object for the event.
					 * @param {FooTable.Table} ft - The instance of the plugin raising the event.
					 * @param {FooTable.Sorter} sorter - The sorter that has been applied.
					 */
					self.ft.raise('after.ft.sorting', [sorter]);
				});
			});
		},
		/**
		 * Handles the sort button clicked event.
		 * @instance
		 * @private
		 * @param {jQuery.Event} e - The event object for the event.
		 */
		_onSortClicked: function (e) {
			e.preventDefault();
			var self = e.data.self, $header = $(this).closest('th,td'),
				direction = $header.is('.footable-asc, .footable-desc')
					? ($header.hasClass('footable-desc') ? 'ASC' : 'DESC')
					: 'ASC';
			self._sort($header.index(), direction);
		}
	});

	/**
	 * Checks the supplied string is a valid direction and if not returns ASC as default.
	 * @static
	 * @protected
	 * @param {string} str - The string to check.
	 */
	F.Sorting.dir = function(str){
		return F.is.string(str) && (str == 'ASC' || str == 'DESC') ? str : 'ASC';
	};

	F.components.core.register('sorting', F.Sorting, 5);

})(jQuery, FooTable);