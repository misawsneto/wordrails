function safeApply(scope, fn) {
  var phase = null
  if(scope.$root && scope.$root.$$phase)
    phase = scope.$root.$$phase;

  if(phase == '$apply' || phase == '$digest')
    scope.$eval(fn);
  else
    scope.$apply(fn);
}
