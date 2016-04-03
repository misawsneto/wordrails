(function() {
  'use strict';

  /**
   * @ngdoc overview
   * @name ui.grid.pagination
   *
   * @description
   *
   * # ui.grid.pagination
   *
   * <div class="alert alert-warning" role="alert"><strong>Alpha</strong> This feature is in development. There will almost certainly be breaking api changes, or there are major outstanding bugs.</div>
   *
   * This module provides pagination support to ui-grid
   */
  var module = angular.module('ui.grid.pagination', ['ng', 'ui.grid']);

  /**
   * @ngdoc service
   * @name ui.grid.pagination.service:uiGridPaginationService
   *
   * @description Service for the pagination feature
   */
  module.service('uiGridPaginationService', ['gridUtil',
    function (gridUtil) {
      var service = {
        /**
         * @ngdoc method
         * @name initializeGrid
         * @methodOf ui.grid.pagination.service:uiGridPaginationService
         * @description Attaches the service to a certain grid
         * @param {Grid} grid The grid we want to work with
         */
        initializeGrid: function (grid) {
          service.defaultGridOptions(grid.options);

          /**
          * @ngdoc object
          * @name ui.grid.pagination.api:PublicAPI
          *
          * @description Public API for the pagination feature
          */
          var publicApi = {
            events: {
              pagination: {
              /**
               * @ngdoc event
               * @name paginationChanged
               * @eventOf ui.grid.pagination.api:PublicAPI
               * @description This event fires when the pageSize or currentPage changes
               * @param {int} currentPage requested page number
               * @param {int} pageSize requested page size
               */
                paginationChanged: function (currentPage, pageSize) { }
              }
            },
            methods: {
              pagination: {
                /**
                 * @ngdoc method
                 * @name getPage
                 * @methodOf ui.grid.pagination.api:PublicAPI
                 * @description Returns the number of the current page
                 */
                getPage: function () {
                  return grid.options.enablePagination ? grid.options.paginationCurrentPage : null;
                },
                /**
                 * @ngdoc method
                 * @name getTotalPages
                 * @methodOf ui.grid.pagination.api:PublicAPI
                 * @description Returns the total number of pages
                 */
                getTotalPages: function () {
                  if (!grid.options.enablePagination) {
                    return null;
                  }

                  return (grid.options.totalItems === 0) ? 1 : Math.ceil(grid.options.totalItems / grid.options.paginationPageSize);
                },
                /**
                 * @ngdoc method
                 * @name nextPage
                 * @methodOf ui.grid.pagination.api:PublicAPI
                 * @description Moves to the next page, if possible
                 */
                nextPage: function () {
                  if (!grid.options.enablePagination) {
                    return;
                  }

                  if (grid.options.totalItems > 0) {
                    grid.options.paginationCurrentPage = Math.min(
                      grid.options.paginationCurrentPage + 1,
                      publicApi.methods.pagination.getTotalPages()
                    );
                  } else {
                    grid.options.paginationCurrentPage++;
                  }
                },
                /**
                 * @ngdoc method
                 * @name previousPage
                 * @methodOf ui.grid.pagination.api:PublicAPI
                 * @description Moves to the previous page, if we're not on the first page
                 */
                previousPage: function () {
                  if (!grid.options.enablePagination) {
                    return;
                  }

                  grid.options.paginationCurrentPage = Math.max(grid.options.paginationCurrentPage - 1, 1);
                },
                /**
                 * @ngdoc method
                 * @name seek
                 * @methodOf ui.grid.pagination.api:PublicAPI
                 * @description Moves to the requested page
                 * @param {int} page The number of the page that should be displayed
                 */
                seek: function (page) {
                  if (!grid.options.enablePagination) {
                    return;
                  }
                  if (!angular.isNumber(page) || page < 1) {
                    throw 'Invalid page number: ' + page;
                  }

                  grid.options.paginationCurrentPage = Math.min(page, publicApi.methods.pagination.getTotalPages());
                }
              }
            }
          };

          grid.api.registerEventsFromObject(publicApi.events);
          grid.api.registerMethodsFromObject(publicApi.methods);

          var processPagination = function( renderableRows ){
            if (grid.options.useExternalPagination || !grid.options.enablePagination) {
              return renderableRows;
            }
            //client side pagination
            var pageSize = parseInt(grid.options.paginationPageSize, 10);
            var currentPage = parseInt(grid.options.paginationCurrentPage, 10);

            var visibleRows = renderableRows.filter(function (row) { return row.visible; });
            grid.options.totalItems = visibleRows.length;

            var firstRow = (currentPage - 1) * pageSize;
            if (firstRow > visibleRows.length) {
              currentPage = grid.options.paginationCurrentPage = 1;
              firstRow = (currentPage - 1) * pageSize;
            }
            return visibleRows.slice(firstRow, firstRow + pageSize);
          };

          grid.registerRowsProcessor(processPagination, 900 );

        },
        defaultGridOptions: function (gridOptions) {
          /**
           * @ngdoc object
           * @name ui.grid.pagination.api:GridOptions
           *
           * @description GridOptions for the pagination feature, these are available to be
           * set using the ui-grid {@link ui.grid.class:GridOptions gridOptions}
           */

          /**
           * @ngdoc property
           * @name enablePagination
           * @propertyOf ui.grid.pagination.api:GridOptions
           * @description Enables pagination.  Defaults to true.
           */
          gridOptions.enablePagination = gridOptions.enablePagination !== false;
          /**
           * @ngdoc property
           * @name enablePaginationControls
           * @propertyOf ui.grid.pagination.api:GridOptions
           * @description Enables the paginator at the bottom of the grid. Turn this off if you want to implement your
           *              own controls outside the grid.
           */
          gridOptions.enablePaginationControls = gridOptions.enablePaginationControls !== false;
          /**
           * @ngdoc property
           * @name useExternalPagination
           * @propertyOf ui.grid.pagination.api:GridOptions
           * @description Disables client side pagination. When true, handle the paginationChanged event and set data
           *              and totalItems.  Defaults to `false`
           */
          gridOptions.useExternalPagination = gridOptions.useExternalPagination === true;
          /**
           * @ngdoc property
           * @name totalItems
           * @propertyOf ui.grid.pagination.api:GridOptions
           * @description Total number of items, set automatically when using client side pagination, but needs set by user
           *              for server side pagination
           */
          if (gridUtil.isNullOrUndefined(gridOptions.totalItems)) {
            gridOptions.totalItems = 0;
          }
          /**
           * @ngdoc property
           * @name paginationPageSizes
           * @propertyOf ui.grid.pagination.api:GridOptions
           * @description Array of page sizes, defaults to `[250, 500, 1000]`
           */
          if (gridUtil.isNullOrUndefined(gridOptions.paginationPageSizes)) {
            gridOptions.paginationPageSizes = [250, 500, 1000];
          }
          /**
           * @ngdoc property
           * @name paginationPageSize
           * @propertyOf ui.grid.pagination.api:GridOptions
           * @description Page size, defaults to the first item in paginationPageSizes, or 0 if paginationPageSizes is empty
           */
          if (gridUtil.isNullOrUndefined(gridOptions.paginationPageSize)) {
            if (gridOptions.paginationPageSizes.length > 0) {
              gridOptions.paginationPageSize = gridOptions.paginationPageSizes[0];
            } else {
              gridOptions.paginationPageSize = 0;
            }
          }
          /**
           * @ngdoc property
           * @name paginationCurrentPage
           * @propertyOf ui.grid.pagination.api:GridOptions
           * @description Current page number, defaults to 1
           */
          if (gridUtil.isNullOrUndefined(gridOptions.paginationCurrentPage)) {
            gridOptions.paginationCurrentPage = 1;
          }

          /**
           * @ngdoc property
           * @name paginationTemplate
           * @propertyOf ui.grid.pagination.api:GridOptions
           * @description A custom template for the pager, defaults to `ui-grid/pagination`
           */
          if (gridUtil.isNullOrUndefined(gridOptions.paginationTemplate)) {
            gridOptions.paginationTemplate = 'ui-grid/pagination';
          }
        },
        /**
         * @ngdoc method
         * @methodOf ui.grid.pagination.service:uiGridPaginationService
         * @name uiGridPaginationService
         * @description  Raises paginationChanged and calls refresh for client side pagination
         * @param {Grid} grid the grid for which the pagination changed
         * @param {int} currentPage requested page number
         * @param {int} pageSize requested page size
         */
        onPaginationChanged: function (grid, currentPage, pageSize) {
            grid.api.pagination.raise.paginationChanged(currentPage, pageSize);
            if (!grid.options.useExternalPagination) {
              grid.queueGridRefresh(); //client side pagination
            }
        }
      };

      return service;
    }
  ]);
  /**
   *  @ngdoc directive
   *  @name ui.grid.pagination.directive:uiGridPagination
   *  @element div
   *  @restrict A
   *
   *  @description Adds pagination features to grid
   *  @example
   <example module="app">
   <file name="app.js">
   var app = angular.module('app', ['ui.grid', 'ui.grid.pagination']);

   app.controller('MainCtrl', ['$scope', function ($scope) {
      $scope.data = [
        { name: 'Alex', car: 'Toyota' },
        { name: 'Sam', car: 'Lexus' },
        { name: 'Joe', car: 'Dodge' },
        { name: 'Bob', car: 'Buick' },
        { name: 'Cindy', car: 'Ford' },
        { name: 'Brian', car: 'Audi' },
        { name: 'Malcom', car: 'Mercedes Benz' },
        { name: 'Dave', car: 'Ford' },
        { name: 'Stacey', car: 'Audi' },
        { name: 'Amy', car: 'Acura' },
        { name: 'Scott', car: 'Toyota' },
        { name: 'Ryan', car: 'BMW' },
      ];

      $scope.gridOptions = {
        data: 'data',
        paginationPageSizes: [5, 10, 25],
        paginationPageSize: 5,
        columnDefs: [
          {name: 'name'},
          {name: 'car'}
        ]
       }
    }]);
   </file>
   <file name="index.html">
   <div ng-controller="MainCtrl">
   <div ui-grid="gridOptions" ui-grid-pagination></div>
   </div>
   </file>
   </example>
   */
  module.directive('uiGridPagination', ['gridUtil', 'uiGridPaginationService',
    function (gridUtil, uiGridPaginationService) {
      return {
        priority: -200,
        scope: false,
        require: 'uiGrid',
        link: {
          pre: function ($scope, $elm, $attr, uiGridCtrl) {
            uiGridPaginationService.initializeGrid(uiGridCtrl.grid);

            gridUtil.getTemplate(uiGridCtrl.grid.options.paginationTemplate)
              .then(function (contents) {
                var template = angular.element(contents);
                $elm.append(template);
                uiGridCtrl.innerCompile(template);
              });
          }
        }
      };
    }
  ]);

  /**
   *  @ngdoc directive
   *  @name ui.grid.pagination.directive:uiGridPager
   *  @element div
   *
   *  @description Panel for handling pagination
   */
  module.directive('uiGridPager', ['uiGridPaginationService', 'uiGridConstants', 'gridUtil', 'i18nService',
    function (uiGridPaginationService, uiGridConstants, gridUtil, i18nService) {
      return {
        priority: -200,
        scope: true,
        require: '^uiGrid',
        link: function ($scope, $elm, $attr, uiGridCtrl) {
          var defaultFocusElementSelector = '.ui-grid-pager-control-input';
          $scope.aria = i18nService.getSafeText('pagination.aria'); //Returns an object with all of the aria labels

          $scope.paginationApi = uiGridCtrl.grid.api.pagination;
          $scope.sizesLabel = i18nService.getSafeText('pagination.sizes');
          $scope.totalItemsLabel = i18nService.getSafeText('pagination.totalItems');
          $scope.paginationOf = i18nService.getSafeText('pagination.of');
          $scope.paginationThrough = i18nService.getSafeText('pagination.through');

          var options = uiGridCtrl.grid.options;

          uiGridCtrl.grid.renderContainers.body.registerViewportAdjuster(function (adjustment) {
            adjustment.height = adjustment.height - gridUtil.elementHeight($elm, "padding");
            return adjustment;
          });

          var dataChangeDereg = uiGridCtrl.grid.registerDataChangeCallback(function (grid) {
            if (!grid.options.useExternalPagination) {
              grid.options.totalItems = grid.rows.length;
            }
          }, [uiGridConstants.dataChange.ROW]);

          $scope.$on('$destroy', dataChangeDereg);

          var setShowing = function () {
            $scope.showingLow = ((options.paginationCurrentPage - 1) * options.paginationPageSize) + 1;
            $scope.showingHigh = Math.min(options.paginationCurrentPage * options.paginationPageSize, options.totalItems);
          };

          var deregT = $scope.$watch('grid.options.totalItems + grid.options.paginationPageSize', setShowing);

          var deregP = $scope.$watch('grid.options.paginationCurrentPage + grid.options.paginationPageSize', function (newValues, oldValues) {
              if (newValues === oldValues || oldValues === undefined) {
                return;
              }

              if (!angular.isNumber(options.paginationCurrentPage) || options.paginationCurrentPage < 1) {
                options.paginationCurrentPage = 1;
                return;
              }

              if (options.totalItems > 0 && options.paginationCurrentPage > $scope.paginationApi.getTotalPages()) {
                options.paginationCurrentPage = $scope.paginationApi.getTotalPages();
                return;
              }

              setShowing();
              uiGridPaginationService.onPaginationChanged($scope.grid, options.paginationCurrentPage, options.paginationPageSize);
            }
          );

          $scope.$on('$destroy', function() {
            deregT();
            deregP();
          });

          $scope.cantPageForward = function () {
            if (options.totalItems > 0) {
              return options.paginationCurrentPage >= $scope.paginationApi.getTotalPages();
            } else {
              return options.data.length < 1;
            }
          };

          $scope.cantPageToLast = function () {
            if (options.totalItems > 0) {
              return $scope.cantPageForward();
            } else {
              return true;
            }
          };

          $scope.cantPageBackward = function () {
            return options.paginationCurrentPage <= 1;
          };

          var focusToInputIf = function(condition){
            if (condition){
              gridUtil.focus.bySelector($elm, defaultFocusElementSelector);
            }
          };

          //Takes care of setting focus to the middle element when focus is lost
          $scope.pageFirstPageClick = function () {
            $scope.paginationApi.seek(1);
            focusToInputIf($scope.cantPageBackward());
          };

          $scope.pagePreviousPageClick = function () {
            $scope.paginationApi.previousPage();
            focusToInputIf($scope.cantPageBackward());
          };

          $scope.pageNextPageClick = function () {
            $scope.paginationApi.nextPage();
            focusToInputIf($scope.cantPageForward());
          };

          $scope.pageLastPageClick = function () {
            $scope.paginationApi.seek($scope.paginationApi.getTotalPages());
            focusToInputIf($scope.cantPageToLast());
          };

        }
      };
    }
  ]);
})();
