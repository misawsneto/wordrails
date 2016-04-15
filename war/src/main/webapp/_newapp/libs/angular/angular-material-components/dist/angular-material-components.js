(function(){angular.module("ngMaterial.components.templates", []).run(["$templateCache", function($templateCache) {$templateCache.put("date-picker/date-picker-dialog.html","<md-dialog class=\"mdc-date-picker\">\n    <!-- Date picker -->\n    <div md-theme=\"{{mdTheme}}\">\n      <!-- Current day of week -->\n      <md-toolbar class=\"md-hue-2 mdc-date-picker__current-day-of-week\">\n        <span>{{ moment(selected.date).format(\'dddd\') }}</span>\n      </md-toolbar>\n\n      <!-- Current date -->\n      <md-toolbar class=\"mdc-date-picker__current-date\">\n        <span>{{ moment(selected.date).format(\'MMM\') }}</span>\n        <strong>{{ moment(selected.date).format(\'DD\') }}</strong>\n        <a ng-click=\"displayYearSelection()\">{{ moment(selected.date).format(\'YYYY\') }}</a>\n      </md-toolbar>\n\n      <!-- Calendar -->\n      <div class=\"mdc-date-picker__calendar\" ng-if=\"!yearSelection\">\n        <div class=\"mdc-date-picker__nav\">\n          <md-button class=\"md-fab md-primary\" aria-label=\"Previous month\" ng-click=\"previousMonth()\">\n            <i class=\"mdi mdi-chevron-left\"></i>\n          </md-button>\n\n          <span>{{ activeDate.format(\'MMMM YYYY\') }}</span>\n\n          <md-button class=\"md-fab md-primary\" arial-label=\"Next month\" ng-click=\"nextMonth()\">\n            <i class=\"mdi mdi-chevron-right\"></i>\n          </md-button>\n        </div>\n\n        <div class=\"mdc-date-picker__days-of-week\">\n          <span ng-repeat=\"day in daysOfWeek\">{{ day }}</span>\n        </div>\n\n        <div class=\"mdc-date-picker__days\">\n                    <span class=\"mdc-date-picker__day mdc-date-picker__day--is-empty\"\n                          ng-repeat=\"x in emptyFirstDays\">&nbsp;</span><!--\n\n                 --><div class=\"mdc-date-picker__day\"\n                         ng-class=\"{ \'mdc-date-picker__day--is-selected\': day.selected,\n                                     \'mdc-date-picker__day--is-today\': day.today }\"\n                         ng-repeat=\"day in days\">\n          <a ng-click=\"select(day)\">{{ day ? day.format(\'D\') : \'\' }}</a>\n        </div><!--\n\n                 --><span class=\"mdc-date-picker__day mdc-date-picker__day--is-empty\"\n                          ng-repeat=\"x in emptyLastDays\">&nbsp;</span>\n        </div>\n      </div>\n\n      <!-- Year selection -->\n      <div class=\"mdc-date-picker__year-selector\" ng-show=\"yearSelection\">\n        <a class=\"mdc-date-picker__year\"\n           ng-class=\"{ \'mdc-date-picker__year--is-active\': year == activeDate.format(\'YYYY\') }\"\n           ng-repeat=\"year in years\"\n           ng-click=\"selectYear(year)\"\n           ng-if=\"yearSelection\">\n          <span>{{year}}</span>\n        </a>\n      </div>\n\n      <!-- Actions -->\n      <div class=\"md-actions mdc-date-picker__actions\" layout=\"row\" layout-align=\"end\">\n        <md-button class=\"md-primary\" ng-click=\"cancel()\">Cancel</md-button>\n        <md-button class=\"md-primary\" ng-click=\"closePicker()\">Ok</md-button>\n      </div>\n    </div>\n</md-dialog>\n");
$templateCache.put("date-picker/date-picker-input.html","<md-input-container ng-click=\"openPicker($event)\">\n  <label>{{label}}</label>\n  <input type=\"text\" ng-model=\"selected.model\" ng-disabled=\"true\" ng-click=\"openPicker($event)\">\n</md-input-container>\n");}]);})();
(function(){/* global angular */
/* global moment */
/* global navigator */
'use strict'; // jshint ignore:line


angular.module('ngMaterial.components.datePicker', ['ngMaterial'])
.controller('mdcDatePickerController', function ($scope, $timeout, $mdDialog, $document, model, locale, mdTheme) {
    function checkLocale(locale) {
      if (!locale) {
        return (navigator.language !== null ? navigator.language : navigator.browserLanguage).split('_')[0].split('-')[0] || 'en';
      }
      return locale;
    }

    $scope.model = model;
    $scope.mdTheme = mdTheme ? mdTheme : 'default';

    var activeLocale;

    this.build = function (locale) {
      activeLocale = locale;

      moment.locale(activeLocale);

      if (angular.isDefined($scope.model)) {
        $scope.selected = {
          model: moment($scope.model).format('LL'),
          date: $scope.model
        };

        $scope.activeDate = moment($scope.model);
      }
      else {
        $scope.selected = {
          model: undefined,
          date: new Date()
        };

        $scope.activeDate = moment();
      }

      $scope.moment = moment;

      $scope.days = [];
      //TODO: Use moment locale to set first day of week properly.
      $scope.daysOfWeek = [moment.weekdaysMin(1), moment.weekdaysMin(2), moment.weekdaysMin(3), moment.weekdaysMin(4), moment.weekdaysMin(5), moment.weekdaysMin(6), moment.weekdaysMin(0)];

      $scope.years = [];

      for (var y = moment().year() - 100; y <= moment().year() + 100; y++) {
        $scope.years.push(y);
      }

      generateCalendar();
    };
    this.build(checkLocale(locale));

    $scope.previousMonth = function () {
      $scope.activeDate = $scope.activeDate.subtract(1, 'month');
      generateCalendar();
    };

    $scope.nextMonth = function () {
      $scope.activeDate = $scope.activeDate.add(1, 'month');
      generateCalendar();
    };

    $scope.select = function (day) {
      $scope.selected = {
        model: day.format('LL'),
        date: day.toDate()
      };

      $scope.model = day.toDate();

      generateCalendar();
    };

    $scope.selectYear = function (year) {
      $scope.yearSelection = false;

      $scope.selected.model = moment($scope.selected.date).year(year).format('LL');
      $scope.selected.date = moment($scope.selected.date).year(year).toDate();
      $scope.model = moment($scope.selected.date).toDate();
      $scope.activeDate = $scope.activeDate.add(year - $scope.activeDate.year(), 'year');

      generateCalendar();
    };
    $scope.displayYearSelection = function () {
      var calendarHeight = $document[0].getElementsByClassName('mdc-date-picker__calendar')[0].offsetHeight;
      var yearSelectorElement = $document[0].getElementsByClassName('mdc-date-picker__year-selector')[0];
      yearSelectorElement.style.height = calendarHeight + 'px';

      $scope.yearSelection = true;

      $timeout(function () {
        var activeYearElement = $document[0].getElementsByClassName('mdc-date-picker__year--is-active')[0];
        yearSelectorElement.scrollTop = yearSelectorElement.scrollTop + activeYearElement.offsetTop - yearSelectorElement.offsetHeight / 2 + activeYearElement.offsetHeight / 2;
      });
    };

    function generateCalendar() {
      var days = [],
        previousDay = angular.copy($scope.activeDate).date(0),
        firstDayOfMonth = angular.copy($scope.activeDate).date(1),
        lastDayOfMonth = angular.copy(firstDayOfMonth).endOf('month'),
        maxDays = angular.copy(lastDayOfMonth).date();

      $scope.emptyFirstDays = [];

      for (var i = firstDayOfMonth.day() === 0 ? 6 : firstDayOfMonth.day() - 1; i > 0; i--) {
        $scope.emptyFirstDays.push({});
      }

      for (var j = 0; j < maxDays; j++) {
        var date = angular.copy(previousDay.add(1, 'days'));

        date.selected = angular.isDefined($scope.selected.model) && date.isSame($scope.selected.date, 'day');
        date.today = date.isSame(moment(), 'day');

        days.push(date);
      }

      $scope.emptyLastDays = [];

      for (var k = 7 - (lastDayOfMonth.day() === 0 ? 7 : lastDayOfMonth.day()); k > 0; k--) {
        $scope.emptyLastDays.push({});
      }

      $scope.days = days;
    }

    $scope.cancel = function() {
      $mdDialog.hide();
    };

    $scope.closePicker = function () {
      $mdDialog.hide($scope.selected);
    };
  })
.controller('mdcDatePickerInputController', function ($scope, $attrs, $timeout, $mdDialog) {
    if (angular.isDefined($scope.model)) {
      $scope.selected = {
        model: moment($scope.model).format('LL'),
        date: $scope.model
      };
    }
    else {
      $scope.selected = {
        model: undefined,
        date: new Date()
      };
    }

    $scope.openPicker = function (ev) {
      $scope.yearSelection = false;

      $mdDialog.show({
        targetEvent: ev,
        templateUrl: 'date-picker/date-picker-dialog.html',
        controller: 'mdcDatePickerController',
        locals: {model: $scope.model, locale: $attrs.locale, mdTheme: $attrs.dialogMdTheme}
      }).then(function (selected) {
        if (selected) {
          $scope.selected = selected;
          $scope.model = selected.model;
        }
      });
    };
  })
.directive('mdcDatePicker', function () {
    return {
      restrict: 'AE',
      controller: 'mdcDatePickerInputController',
      scope: {
        model: '=',
        label: '@'
      },
      templateUrl: 'date-picker/date-picker-input.html'
    };
  });
})();
(function(){'use strict';

angular.module('ngMaterial.components', [
  'ngMaterial',
  'ngMaterial.components.templates',
  'ngMaterial.components.datePicker'
]);
})();
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi50bXAvdGVtcGxhdGVzL3RlbXBsYXRlcy5qcyIsInNyYy9kYXRlLXBpY2tlci9kYXRlLXBpY2tlci5qcyIsInNyYy9jb21wb25lbnRzLmpzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiJBQUFBO0FBQ0E7QUNEQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUN0TEE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSIsImZpbGUiOiJhbmd1bGFyLW1hdGVyaWFsLWNvbXBvbmVudHMuanMiLCJzb3VyY2VzQ29udGVudCI6WyJhbmd1bGFyLm1vZHVsZShcIm5nTWF0ZXJpYWwuY29tcG9uZW50cy50ZW1wbGF0ZXNcIiwgW10pLnJ1bihbXCIkdGVtcGxhdGVDYWNoZVwiLCBmdW5jdGlvbigkdGVtcGxhdGVDYWNoZSkgeyR0ZW1wbGF0ZUNhY2hlLnB1dChcImRhdGUtcGlja2VyL2RhdGUtcGlja2VyLWRpYWxvZy5odG1sXCIsXCI8bWQtZGlhbG9nIGNsYXNzPVxcXCJtZGMtZGF0ZS1waWNrZXJcXFwiPlxcbiAgICA8IS0tIERhdGUgcGlja2VyIC0tPlxcbiAgICA8ZGl2IG1kLXRoZW1lPVxcXCJ7e21kVGhlbWV9fVxcXCI+XFxuICAgICAgPCEtLSBDdXJyZW50IGRheSBvZiB3ZWVrIC0tPlxcbiAgICAgIDxtZC10b29sYmFyIGNsYXNzPVxcXCJtZC1odWUtMiBtZGMtZGF0ZS1waWNrZXJfX2N1cnJlbnQtZGF5LW9mLXdlZWtcXFwiPlxcbiAgICAgICAgPHNwYW4+e3sgbW9tZW50KHNlbGVjdGVkLmRhdGUpLmZvcm1hdChcXCdkZGRkXFwnKSB9fTwvc3Bhbj5cXG4gICAgICA8L21kLXRvb2xiYXI+XFxuXFxuICAgICAgPCEtLSBDdXJyZW50IGRhdGUgLS0+XFxuICAgICAgPG1kLXRvb2xiYXIgY2xhc3M9XFxcIm1kYy1kYXRlLXBpY2tlcl9fY3VycmVudC1kYXRlXFxcIj5cXG4gICAgICAgIDxzcGFuPnt7IG1vbWVudChzZWxlY3RlZC5kYXRlKS5mb3JtYXQoXFwnTU1NXFwnKSB9fTwvc3Bhbj5cXG4gICAgICAgIDxzdHJvbmc+e3sgbW9tZW50KHNlbGVjdGVkLmRhdGUpLmZvcm1hdChcXCdERFxcJykgfX08L3N0cm9uZz5cXG4gICAgICAgIDxhIG5nLWNsaWNrPVxcXCJkaXNwbGF5WWVhclNlbGVjdGlvbigpXFxcIj57eyBtb21lbnQoc2VsZWN0ZWQuZGF0ZSkuZm9ybWF0KFxcJ1lZWVlcXCcpIH19PC9hPlxcbiAgICAgIDwvbWQtdG9vbGJhcj5cXG5cXG4gICAgICA8IS0tIENhbGVuZGFyIC0tPlxcbiAgICAgIDxkaXYgY2xhc3M9XFxcIm1kYy1kYXRlLXBpY2tlcl9fY2FsZW5kYXJcXFwiIG5nLWlmPVxcXCIheWVhclNlbGVjdGlvblxcXCI+XFxuICAgICAgICA8ZGl2IGNsYXNzPVxcXCJtZGMtZGF0ZS1waWNrZXJfX25hdlxcXCI+XFxuICAgICAgICAgIDxtZC1idXR0b24gY2xhc3M9XFxcIm1kLWZhYiBtZC1wcmltYXJ5XFxcIiBhcmlhLWxhYmVsPVxcXCJQcmV2aW91cyBtb250aFxcXCIgbmctY2xpY2s9XFxcInByZXZpb3VzTW9udGgoKVxcXCI+XFxuICAgICAgICAgICAgPGkgY2xhc3M9XFxcIm1kaSBtZGktY2hldnJvbi1sZWZ0XFxcIj48L2k+XFxuICAgICAgICAgIDwvbWQtYnV0dG9uPlxcblxcbiAgICAgICAgICA8c3Bhbj57eyBhY3RpdmVEYXRlLmZvcm1hdChcXCdNTU1NIFlZWVlcXCcpIH19PC9zcGFuPlxcblxcbiAgICAgICAgICA8bWQtYnV0dG9uIGNsYXNzPVxcXCJtZC1mYWIgbWQtcHJpbWFyeVxcXCIgYXJpYWwtbGFiZWw9XFxcIk5leHQgbW9udGhcXFwiIG5nLWNsaWNrPVxcXCJuZXh0TW9udGgoKVxcXCI+XFxuICAgICAgICAgICAgPGkgY2xhc3M9XFxcIm1kaSBtZGktY2hldnJvbi1yaWdodFxcXCI+PC9pPlxcbiAgICAgICAgICA8L21kLWJ1dHRvbj5cXG4gICAgICAgIDwvZGl2PlxcblxcbiAgICAgICAgPGRpdiBjbGFzcz1cXFwibWRjLWRhdGUtcGlja2VyX19kYXlzLW9mLXdlZWtcXFwiPlxcbiAgICAgICAgICA8c3BhbiBuZy1yZXBlYXQ9XFxcImRheSBpbiBkYXlzT2ZXZWVrXFxcIj57eyBkYXkgfX08L3NwYW4+XFxuICAgICAgICA8L2Rpdj5cXG5cXG4gICAgICAgIDxkaXYgY2xhc3M9XFxcIm1kYy1kYXRlLXBpY2tlcl9fZGF5c1xcXCI+XFxuICAgICAgICAgICAgICAgICAgICA8c3BhbiBjbGFzcz1cXFwibWRjLWRhdGUtcGlja2VyX19kYXkgbWRjLWRhdGUtcGlja2VyX19kYXktLWlzLWVtcHR5XFxcIlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgbmctcmVwZWF0PVxcXCJ4IGluIGVtcHR5Rmlyc3REYXlzXFxcIj4mbmJzcDs8L3NwYW4+PCEtLVxcblxcbiAgICAgICAgICAgICAgICAgLS0+PGRpdiBjbGFzcz1cXFwibWRjLWRhdGUtcGlja2VyX19kYXlcXFwiXFxuICAgICAgICAgICAgICAgICAgICAgICAgIG5nLWNsYXNzPVxcXCJ7IFxcJ21kYy1kYXRlLXBpY2tlcl9fZGF5LS1pcy1zZWxlY3RlZFxcJzogZGF5LnNlbGVjdGVkLFxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBcXCdtZGMtZGF0ZS1waWNrZXJfX2RheS0taXMtdG9kYXlcXCc6IGRheS50b2RheSB9XFxcIlxcbiAgICAgICAgICAgICAgICAgICAgICAgICBuZy1yZXBlYXQ9XFxcImRheSBpbiBkYXlzXFxcIj5cXG4gICAgICAgICAgPGEgbmctY2xpY2s9XFxcInNlbGVjdChkYXkpXFxcIj57eyBkYXkgPyBkYXkuZm9ybWF0KFxcJ0RcXCcpIDogXFwnXFwnIH19PC9hPlxcbiAgICAgICAgPC9kaXY+PCEtLVxcblxcbiAgICAgICAgICAgICAgICAgLS0+PHNwYW4gY2xhc3M9XFxcIm1kYy1kYXRlLXBpY2tlcl9fZGF5IG1kYy1kYXRlLXBpY2tlcl9fZGF5LS1pcy1lbXB0eVxcXCJcXG4gICAgICAgICAgICAgICAgICAgICAgICAgIG5nLXJlcGVhdD1cXFwieCBpbiBlbXB0eUxhc3REYXlzXFxcIj4mbmJzcDs8L3NwYW4+XFxuICAgICAgICA8L2Rpdj5cXG4gICAgICA8L2Rpdj5cXG5cXG4gICAgICA8IS0tIFllYXIgc2VsZWN0aW9uIC0tPlxcbiAgICAgIDxkaXYgY2xhc3M9XFxcIm1kYy1kYXRlLXBpY2tlcl9feWVhci1zZWxlY3RvclxcXCIgbmctc2hvdz1cXFwieWVhclNlbGVjdGlvblxcXCI+XFxuICAgICAgICA8YSBjbGFzcz1cXFwibWRjLWRhdGUtcGlja2VyX195ZWFyXFxcIlxcbiAgICAgICAgICAgbmctY2xhc3M9XFxcInsgXFwnbWRjLWRhdGUtcGlja2VyX195ZWFyLS1pcy1hY3RpdmVcXCc6IHllYXIgPT0gYWN0aXZlRGF0ZS5mb3JtYXQoXFwnWVlZWVxcJykgfVxcXCJcXG4gICAgICAgICAgIG5nLXJlcGVhdD1cXFwieWVhciBpbiB5ZWFyc1xcXCJcXG4gICAgICAgICAgIG5nLWNsaWNrPVxcXCJzZWxlY3RZZWFyKHllYXIpXFxcIlxcbiAgICAgICAgICAgbmctaWY9XFxcInllYXJTZWxlY3Rpb25cXFwiPlxcbiAgICAgICAgICA8c3Bhbj57e3llYXJ9fTwvc3Bhbj5cXG4gICAgICAgIDwvYT5cXG4gICAgICA8L2Rpdj5cXG5cXG4gICAgICA8IS0tIEFjdGlvbnMgLS0+XFxuICAgICAgPGRpdiBjbGFzcz1cXFwibWQtYWN0aW9ucyBtZGMtZGF0ZS1waWNrZXJfX2FjdGlvbnNcXFwiIGxheW91dD1cXFwicm93XFxcIiBsYXlvdXQtYWxpZ249XFxcImVuZFxcXCI+XFxuICAgICAgICA8bWQtYnV0dG9uIGNsYXNzPVxcXCJtZC1wcmltYXJ5XFxcIiBuZy1jbGljaz1cXFwiY2FuY2VsKClcXFwiPkNhbmNlbDwvbWQtYnV0dG9uPlxcbiAgICAgICAgPG1kLWJ1dHRvbiBjbGFzcz1cXFwibWQtcHJpbWFyeVxcXCIgbmctY2xpY2s9XFxcImNsb3NlUGlja2VyKClcXFwiPk9rPC9tZC1idXR0b24+XFxuICAgICAgPC9kaXY+XFxuICAgIDwvZGl2PlxcbjwvbWQtZGlhbG9nPlxcblwiKTtcbiR0ZW1wbGF0ZUNhY2hlLnB1dChcImRhdGUtcGlja2VyL2RhdGUtcGlja2VyLWlucHV0Lmh0bWxcIixcIjxtZC1pbnB1dC1jb250YWluZXIgbmctY2xpY2s9XFxcIm9wZW5QaWNrZXIoJGV2ZW50KVxcXCI+XFxuICA8bGFiZWw+e3tsYWJlbH19PC9sYWJlbD5cXG4gIDxpbnB1dCB0eXBlPVxcXCJ0ZXh0XFxcIiBuZy1tb2RlbD1cXFwic2VsZWN0ZWQubW9kZWxcXFwiIG5nLWRpc2FibGVkPVxcXCJ0cnVlXFxcIiBuZy1jbGljaz1cXFwib3BlblBpY2tlcigkZXZlbnQpXFxcIj5cXG48L21kLWlucHV0LWNvbnRhaW5lcj5cXG5cIik7fV0pOyIsIi8qIGdsb2JhbCBhbmd1bGFyICovXG4vKiBnbG9iYWwgbW9tZW50ICovXG4vKiBnbG9iYWwgbmF2aWdhdG9yICovXG4ndXNlIHN0cmljdCc7IC8vIGpzaGludCBpZ25vcmU6bGluZVxuXG5cbmFuZ3VsYXIubW9kdWxlKCduZ01hdGVyaWFsLmNvbXBvbmVudHMuZGF0ZVBpY2tlcicsIFsnbmdNYXRlcmlhbCddKVxuLmNvbnRyb2xsZXIoJ21kY0RhdGVQaWNrZXJDb250cm9sbGVyJywgZnVuY3Rpb24gKCRzY29wZSwgJHRpbWVvdXQsICRtZERpYWxvZywgJGRvY3VtZW50LCBtb2RlbCwgbG9jYWxlLCBtZFRoZW1lKSB7XG4gICAgZnVuY3Rpb24gY2hlY2tMb2NhbGUobG9jYWxlKSB7XG4gICAgICBpZiAoIWxvY2FsZSkge1xuICAgICAgICByZXR1cm4gKG5hdmlnYXRvci5sYW5ndWFnZSAhPT0gbnVsbCA/IG5hdmlnYXRvci5sYW5ndWFnZSA6IG5hdmlnYXRvci5icm93c2VyTGFuZ3VhZ2UpLnNwbGl0KCdfJylbMF0uc3BsaXQoJy0nKVswXSB8fCAnZW4nO1xuICAgICAgfVxuICAgICAgcmV0dXJuIGxvY2FsZTtcbiAgICB9XG5cbiAgICAkc2NvcGUubW9kZWwgPSBtb2RlbDtcbiAgICAkc2NvcGUubWRUaGVtZSA9IG1kVGhlbWUgPyBtZFRoZW1lIDogJ2RlZmF1bHQnO1xuXG4gICAgdmFyIGFjdGl2ZUxvY2FsZTtcblxuICAgIHRoaXMuYnVpbGQgPSBmdW5jdGlvbiAobG9jYWxlKSB7XG4gICAgICBhY3RpdmVMb2NhbGUgPSBsb2NhbGU7XG5cbiAgICAgIG1vbWVudC5sb2NhbGUoYWN0aXZlTG9jYWxlKTtcblxuICAgICAgaWYgKGFuZ3VsYXIuaXNEZWZpbmVkKCRzY29wZS5tb2RlbCkpIHtcbiAgICAgICAgJHNjb3BlLnNlbGVjdGVkID0ge1xuICAgICAgICAgIG1vZGVsOiBtb21lbnQoJHNjb3BlLm1vZGVsKS5mb3JtYXQoJ0xMJyksXG4gICAgICAgICAgZGF0ZTogJHNjb3BlLm1vZGVsXG4gICAgICAgIH07XG5cbiAgICAgICAgJHNjb3BlLmFjdGl2ZURhdGUgPSBtb21lbnQoJHNjb3BlLm1vZGVsKTtcbiAgICAgIH1cbiAgICAgIGVsc2Uge1xuICAgICAgICAkc2NvcGUuc2VsZWN0ZWQgPSB7XG4gICAgICAgICAgbW9kZWw6IHVuZGVmaW5lZCxcbiAgICAgICAgICBkYXRlOiBuZXcgRGF0ZSgpXG4gICAgICAgIH07XG5cbiAgICAgICAgJHNjb3BlLmFjdGl2ZURhdGUgPSBtb21lbnQoKTtcbiAgICAgIH1cblxuICAgICAgJHNjb3BlLm1vbWVudCA9IG1vbWVudDtcblxuICAgICAgJHNjb3BlLmRheXMgPSBbXTtcbiAgICAgIC8vVE9ETzogVXNlIG1vbWVudCBsb2NhbGUgdG8gc2V0IGZpcnN0IGRheSBvZiB3ZWVrIHByb3Blcmx5LlxuICAgICAgJHNjb3BlLmRheXNPZldlZWsgPSBbbW9tZW50LndlZWtkYXlzTWluKDEpLCBtb21lbnQud2Vla2RheXNNaW4oMiksIG1vbWVudC53ZWVrZGF5c01pbigzKSwgbW9tZW50LndlZWtkYXlzTWluKDQpLCBtb21lbnQud2Vla2RheXNNaW4oNSksIG1vbWVudC53ZWVrZGF5c01pbig2KSwgbW9tZW50LndlZWtkYXlzTWluKDApXTtcblxuICAgICAgJHNjb3BlLnllYXJzID0gW107XG5cbiAgICAgIGZvciAodmFyIHkgPSBtb21lbnQoKS55ZWFyKCkgLSAxMDA7IHkgPD0gbW9tZW50KCkueWVhcigpICsgMTAwOyB5KyspIHtcbiAgICAgICAgJHNjb3BlLnllYXJzLnB1c2goeSk7XG4gICAgICB9XG5cbiAgICAgIGdlbmVyYXRlQ2FsZW5kYXIoKTtcbiAgICB9O1xuICAgIHRoaXMuYnVpbGQoY2hlY2tMb2NhbGUobG9jYWxlKSk7XG5cbiAgICAkc2NvcGUucHJldmlvdXNNb250aCA9IGZ1bmN0aW9uICgpIHtcbiAgICAgICRzY29wZS5hY3RpdmVEYXRlID0gJHNjb3BlLmFjdGl2ZURhdGUuc3VidHJhY3QoMSwgJ21vbnRoJyk7XG4gICAgICBnZW5lcmF0ZUNhbGVuZGFyKCk7XG4gICAgfTtcblxuICAgICRzY29wZS5uZXh0TW9udGggPSBmdW5jdGlvbiAoKSB7XG4gICAgICAkc2NvcGUuYWN0aXZlRGF0ZSA9ICRzY29wZS5hY3RpdmVEYXRlLmFkZCgxLCAnbW9udGgnKTtcbiAgICAgIGdlbmVyYXRlQ2FsZW5kYXIoKTtcbiAgICB9O1xuXG4gICAgJHNjb3BlLnNlbGVjdCA9IGZ1bmN0aW9uIChkYXkpIHtcbiAgICAgICRzY29wZS5zZWxlY3RlZCA9IHtcbiAgICAgICAgbW9kZWw6IGRheS5mb3JtYXQoJ0xMJyksXG4gICAgICAgIGRhdGU6IGRheS50b0RhdGUoKVxuICAgICAgfTtcblxuICAgICAgJHNjb3BlLm1vZGVsID0gZGF5LnRvRGF0ZSgpO1xuXG4gICAgICBnZW5lcmF0ZUNhbGVuZGFyKCk7XG4gICAgfTtcblxuICAgICRzY29wZS5zZWxlY3RZZWFyID0gZnVuY3Rpb24gKHllYXIpIHtcbiAgICAgICRzY29wZS55ZWFyU2VsZWN0aW9uID0gZmFsc2U7XG5cbiAgICAgICRzY29wZS5zZWxlY3RlZC5tb2RlbCA9IG1vbWVudCgkc2NvcGUuc2VsZWN0ZWQuZGF0ZSkueWVhcih5ZWFyKS5mb3JtYXQoJ0xMJyk7XG4gICAgICAkc2NvcGUuc2VsZWN0ZWQuZGF0ZSA9IG1vbWVudCgkc2NvcGUuc2VsZWN0ZWQuZGF0ZSkueWVhcih5ZWFyKS50b0RhdGUoKTtcbiAgICAgICRzY29wZS5tb2RlbCA9IG1vbWVudCgkc2NvcGUuc2VsZWN0ZWQuZGF0ZSkudG9EYXRlKCk7XG4gICAgICAkc2NvcGUuYWN0aXZlRGF0ZSA9ICRzY29wZS5hY3RpdmVEYXRlLmFkZCh5ZWFyIC0gJHNjb3BlLmFjdGl2ZURhdGUueWVhcigpLCAneWVhcicpO1xuXG4gICAgICBnZW5lcmF0ZUNhbGVuZGFyKCk7XG4gICAgfTtcbiAgICAkc2NvcGUuZGlzcGxheVllYXJTZWxlY3Rpb24gPSBmdW5jdGlvbiAoKSB7XG4gICAgICB2YXIgY2FsZW5kYXJIZWlnaHQgPSAkZG9jdW1lbnRbMF0uZ2V0RWxlbWVudHNCeUNsYXNzTmFtZSgnbWRjLWRhdGUtcGlja2VyX19jYWxlbmRhcicpWzBdLm9mZnNldEhlaWdodDtcbiAgICAgIHZhciB5ZWFyU2VsZWN0b3JFbGVtZW50ID0gJGRvY3VtZW50WzBdLmdldEVsZW1lbnRzQnlDbGFzc05hbWUoJ21kYy1kYXRlLXBpY2tlcl9feWVhci1zZWxlY3RvcicpWzBdO1xuICAgICAgeWVhclNlbGVjdG9yRWxlbWVudC5zdHlsZS5oZWlnaHQgPSBjYWxlbmRhckhlaWdodCArICdweCc7XG5cbiAgICAgICRzY29wZS55ZWFyU2VsZWN0aW9uID0gdHJ1ZTtcblxuICAgICAgJHRpbWVvdXQoZnVuY3Rpb24gKCkge1xuICAgICAgICB2YXIgYWN0aXZlWWVhckVsZW1lbnQgPSAkZG9jdW1lbnRbMF0uZ2V0RWxlbWVudHNCeUNsYXNzTmFtZSgnbWRjLWRhdGUtcGlja2VyX195ZWFyLS1pcy1hY3RpdmUnKVswXTtcbiAgICAgICAgeWVhclNlbGVjdG9yRWxlbWVudC5zY3JvbGxUb3AgPSB5ZWFyU2VsZWN0b3JFbGVtZW50LnNjcm9sbFRvcCArIGFjdGl2ZVllYXJFbGVtZW50Lm9mZnNldFRvcCAtIHllYXJTZWxlY3RvckVsZW1lbnQub2Zmc2V0SGVpZ2h0IC8gMiArIGFjdGl2ZVllYXJFbGVtZW50Lm9mZnNldEhlaWdodCAvIDI7XG4gICAgICB9KTtcbiAgICB9O1xuXG4gICAgZnVuY3Rpb24gZ2VuZXJhdGVDYWxlbmRhcigpIHtcbiAgICAgIHZhciBkYXlzID0gW10sXG4gICAgICAgIHByZXZpb3VzRGF5ID0gYW5ndWxhci5jb3B5KCRzY29wZS5hY3RpdmVEYXRlKS5kYXRlKDApLFxuICAgICAgICBmaXJzdERheU9mTW9udGggPSBhbmd1bGFyLmNvcHkoJHNjb3BlLmFjdGl2ZURhdGUpLmRhdGUoMSksXG4gICAgICAgIGxhc3REYXlPZk1vbnRoID0gYW5ndWxhci5jb3B5KGZpcnN0RGF5T2ZNb250aCkuZW5kT2YoJ21vbnRoJyksXG4gICAgICAgIG1heERheXMgPSBhbmd1bGFyLmNvcHkobGFzdERheU9mTW9udGgpLmRhdGUoKTtcblxuICAgICAgJHNjb3BlLmVtcHR5Rmlyc3REYXlzID0gW107XG5cbiAgICAgIGZvciAodmFyIGkgPSBmaXJzdERheU9mTW9udGguZGF5KCkgPT09IDAgPyA2IDogZmlyc3REYXlPZk1vbnRoLmRheSgpIC0gMTsgaSA+IDA7IGktLSkge1xuICAgICAgICAkc2NvcGUuZW1wdHlGaXJzdERheXMucHVzaCh7fSk7XG4gICAgICB9XG5cbiAgICAgIGZvciAodmFyIGogPSAwOyBqIDwgbWF4RGF5czsgaisrKSB7XG4gICAgICAgIHZhciBkYXRlID0gYW5ndWxhci5jb3B5KHByZXZpb3VzRGF5LmFkZCgxLCAnZGF5cycpKTtcblxuICAgICAgICBkYXRlLnNlbGVjdGVkID0gYW5ndWxhci5pc0RlZmluZWQoJHNjb3BlLnNlbGVjdGVkLm1vZGVsKSAmJiBkYXRlLmlzU2FtZSgkc2NvcGUuc2VsZWN0ZWQuZGF0ZSwgJ2RheScpO1xuICAgICAgICBkYXRlLnRvZGF5ID0gZGF0ZS5pc1NhbWUobW9tZW50KCksICdkYXknKTtcblxuICAgICAgICBkYXlzLnB1c2goZGF0ZSk7XG4gICAgICB9XG5cbiAgICAgICRzY29wZS5lbXB0eUxhc3REYXlzID0gW107XG5cbiAgICAgIGZvciAodmFyIGsgPSA3IC0gKGxhc3REYXlPZk1vbnRoLmRheSgpID09PSAwID8gNyA6IGxhc3REYXlPZk1vbnRoLmRheSgpKTsgayA+IDA7IGstLSkge1xuICAgICAgICAkc2NvcGUuZW1wdHlMYXN0RGF5cy5wdXNoKHt9KTtcbiAgICAgIH1cblxuICAgICAgJHNjb3BlLmRheXMgPSBkYXlzO1xuICAgIH1cblxuICAgICRzY29wZS5jYW5jZWwgPSBmdW5jdGlvbigpIHtcbiAgICAgICRtZERpYWxvZy5oaWRlKCk7XG4gICAgfTtcblxuICAgICRzY29wZS5jbG9zZVBpY2tlciA9IGZ1bmN0aW9uICgpIHtcbiAgICAgICRtZERpYWxvZy5oaWRlKCRzY29wZS5zZWxlY3RlZCk7XG4gICAgfTtcbiAgfSlcbi5jb250cm9sbGVyKCdtZGNEYXRlUGlja2VySW5wdXRDb250cm9sbGVyJywgZnVuY3Rpb24gKCRzY29wZSwgJGF0dHJzLCAkdGltZW91dCwgJG1kRGlhbG9nKSB7XG4gICAgaWYgKGFuZ3VsYXIuaXNEZWZpbmVkKCRzY29wZS5tb2RlbCkpIHtcbiAgICAgICRzY29wZS5zZWxlY3RlZCA9IHtcbiAgICAgICAgbW9kZWw6IG1vbWVudCgkc2NvcGUubW9kZWwpLmZvcm1hdCgnTEwnKSxcbiAgICAgICAgZGF0ZTogJHNjb3BlLm1vZGVsXG4gICAgICB9O1xuICAgIH1cbiAgICBlbHNlIHtcbiAgICAgICRzY29wZS5zZWxlY3RlZCA9IHtcbiAgICAgICAgbW9kZWw6IHVuZGVmaW5lZCxcbiAgICAgICAgZGF0ZTogbmV3IERhdGUoKVxuICAgICAgfTtcbiAgICB9XG5cbiAgICAkc2NvcGUub3BlblBpY2tlciA9IGZ1bmN0aW9uIChldikge1xuICAgICAgJHNjb3BlLnllYXJTZWxlY3Rpb24gPSBmYWxzZTtcblxuICAgICAgJG1kRGlhbG9nLnNob3coe1xuICAgICAgICB0YXJnZXRFdmVudDogZXYsXG4gICAgICAgIHRlbXBsYXRlVXJsOiAnZGF0ZS1waWNrZXIvZGF0ZS1waWNrZXItZGlhbG9nLmh0bWwnLFxuICAgICAgICBjb250cm9sbGVyOiAnbWRjRGF0ZVBpY2tlckNvbnRyb2xsZXInLFxuICAgICAgICBsb2NhbHM6IHttb2RlbDogJHNjb3BlLm1vZGVsLCBsb2NhbGU6ICRhdHRycy5sb2NhbGUsIG1kVGhlbWU6ICRhdHRycy5kaWFsb2dNZFRoZW1lfVxuICAgICAgfSkudGhlbihmdW5jdGlvbiAoc2VsZWN0ZWQpIHtcbiAgICAgICAgaWYgKHNlbGVjdGVkKSB7XG4gICAgICAgICAgJHNjb3BlLnNlbGVjdGVkID0gc2VsZWN0ZWQ7XG4gICAgICAgICAgJHNjb3BlLm1vZGVsID0gc2VsZWN0ZWQubW9kZWw7XG4gICAgICAgIH1cbiAgICAgIH0pO1xuICAgIH07XG4gIH0pXG4uZGlyZWN0aXZlKCdtZGNEYXRlUGlja2VyJywgZnVuY3Rpb24gKCkge1xuICAgIHJldHVybiB7XG4gICAgICByZXN0cmljdDogJ0FFJyxcbiAgICAgIGNvbnRyb2xsZXI6ICdtZGNEYXRlUGlja2VySW5wdXRDb250cm9sbGVyJyxcbiAgICAgIHNjb3BlOiB7XG4gICAgICAgIG1vZGVsOiAnPScsXG4gICAgICAgIGxhYmVsOiAnQCdcbiAgICAgIH0sXG4gICAgICB0ZW1wbGF0ZVVybDogJ2RhdGUtcGlja2VyL2RhdGUtcGlja2VyLWlucHV0Lmh0bWwnXG4gICAgfTtcbiAgfSk7XG4iLCIndXNlIHN0cmljdCc7XG5cbmFuZ3VsYXIubW9kdWxlKCduZ01hdGVyaWFsLmNvbXBvbmVudHMnLCBbXG4gICduZ01hdGVyaWFsJyxcbiAgJ25nTWF0ZXJpYWwuY29tcG9uZW50cy50ZW1wbGF0ZXMnLFxuICAnbmdNYXRlcmlhbC5jb21wb25lbnRzLmRhdGVQaWNrZXInXG5dKTtcbiJdLCJzb3VyY2VSb290IjoiL3NvdXJjZS8ifQ==