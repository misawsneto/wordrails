 angular.module('app')
 .directive('focusMe', function($timeout, $parse) {
  return {
    //scope: true,   // optionally create a child scope
    link: function(scope, element, attrs) {
      var model = $parse(attrs.focusMe);
      scope.$watch(model, function(value) {
        if(value === true) { 
          $timeout(function() {
            element[0].focus(); 
          });
        }
      });
    }
  };
})

//  angular.module('app').directive("offcanvas", function(){
//   return{
//     restrict: 'E',
//     link: function(scope, element, attrs){
//       var bodyEl = document.body,
//       content = document.querySelector( '.content-wrap' ),
//       openbtn = document.getElementById( 'open-button' ),
//       closebtn = document.getElementById( 'close-button' ),
//       isOpen = false;

//       function init() {
//         initEvents();
//       }

//       function initEvents() {
//         /*openbtn.addEventListener( 'click', toggleMenu );
//         if( closebtn ) {
//           closebtn.addEventListener( 'click', toggleMenu );
//         }*/

//         // close the menu element if the target it´s not the menu element or one of its descendants..
//         content.addEventListener( 'click', function(ev) {
//           var target = ev.target;
//           if( isOpen && target !== openbtn ) {
//             toggleMenu();
//           }
//         } );
//       }

//       scope.toggleMenu = function(event, close){
//         toggleMenu(close);
//         if(event)
//           event.stopPropagation();
//       }

//       scope.contentClick = function(){
//         if(isOpen)
//           toggleMenu(); 
//       };

//       function toggleMenu(close) {
//         if(close){
//           // classie.remove( bodyEl, 'show-menu' );
//           $('body').removeClass('show-menu')
//           isOpen = false;
//           return;
//         }
//         if( isOpen ) {
//           $('body').removeClass('show-menu')
//         }
//         else {
//           $('body').addClass('show-menu')
//         }

//         isOpen = !isOpen;
//       }

//       init();
//     },
//     templateUrl: 'tpl/blocks/offcanvas.html'
//   }// end of return 
// })

 .directive('clickOutside', ['$document', '$parse' ,function ($document, $parse) {
        return {
            restrict: 'A',
            link: function($scope, elem, attr) {
                var classList = (attr.outsideIfNot !== undefined) ? attr.outsideIfNot.replace(', ', ',').split(',') : [],
                    fn = $parse(attr['clickOutside']);

                // add the elements id so it is not counted in the click listening
                if (attr.id !== undefined) {
                    classList.push(attr.id);
                }

                var eventHandler = function(e) {

                    //check if our element already hiden
                    if(angular.element(elem).hasClass("ng-hide")){
                        return;
                    }

                    var i = 0,
                        element;

                    // if there is no click target, no point going on
                    if (!e || !e.target) {
                        return;
                    }

                    // loop through the available elements, looking for classes in the class list that might match and so will eat
                    for (element = e.target; element; element = element.parentNode) {
                        var id = element.id,
                            classNames = element.className,
                            l = classList.length;

                        // loop through the elements id's and classnames looking for exceptions
                        for (i = 0; i < l; i++) {
                            // check for id's or classes, but only if they exist in the first place
                            if ((id !== undefined && id.indexOf(classList[i]) > -1) || (classNames && classNames.indexOf(classList[i]) > -1)) {
                                // now let's exit out as it is an element that has been defined as being ignored for clicking outside
                                return;
                            }
                        }
                    }

                    // if we have got this far, then we are good to go with processing the command passed in via the click-outside attribute
                    return $scope.$apply(function () {
                        return fn($scope);
                    });
                };

                // assign the document click handler to a variable so we can un-register it when the directive is destroyed
                $document.on('click', eventHandler);

                // when the scope is destroyed, clean up the documents click handler as we don't want it hanging around
                $scope.$on('$destroy', function() {
                    $document.off('click', eventHandler);
                });
            }
        };
    }])

// Module for the demo
angular.module('splash', ['ui.splash']);

// Re-usable $splash module
angular.module('ui.splash', ['ui.bootstrap'])
.service('$splash', [
  '$uibModal',
  '$rootScope',
  function($uibModal, $rootScope) {
    return {
      open: function (opts) {
        if(!opts){
          opts = {};
        }
        opts.backdrop = false;
        opts.windowTemplateUrl = 'splash_index.html';

        return $uibModal.open(opts);
      }
    };
  }
  ])
.run([
  '$templateCache',
  function ($templateCache) {
    $templateCache.put('splash_index.html',
      "<div modal-render=\"{{$isRendered}}\" tabindex=\"-1\" role=\"dialog\" class=\"splash\"\n" +
      "    modal-animation-class=\"fade\"\n" +
      " ng-style=\"{'z-index': 2050 + index*10, display: 'block'}\" ng-click=\"close($event);\">\n" +
      "    <div class='splash-inner' modal-transclude></div>\n" +
      "</div>\n" +
      ""
      );
  }
  ]);