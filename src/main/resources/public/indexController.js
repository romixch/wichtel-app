var app = angular.module('wichtelApp', ['ngResource']);

app.controller('EventController', function($scope, $resource) {
  $scope.title = '';
  $scope.currentName = '';
  $scope.currentEmail = '';
  $scope.wichtels = [];
  $scope.sendingMails = false;
  $scope.triedToSend = false;

  $scope.eventsRes = $resource("/rest/event");
  $scope.eventRes = null;
  $scope.wichtelRes = null;
  $scope.completedRes = null;
  $scope.sendStatusRes = null;

  $scope.blurTitle = function() {
    if ($scope.title === '') { return; }
    if ($scope.eventRes === null) {
      $scope.eventsRes.save({
        name: $scope.title
      }, $scope.onEventSaved);
    } else {
      $scope.eventRes.update({
        name: $scope.title
      });
    }
  };

  $scope.onEventSaved = function(data, headers) {
    $scope.eventRes = $resource(headers('Location'), null, {
      'update': {
        method: 'PUT'
      }
    });
    $scope.eventRes.get($scope.onEventReceived);
  };

  $scope.onEventReceived = function(data) {
    $scope.wichtelRes = $resource(data.links[0].href);
    $scope.completedRes = $resource(data.links[1].href, null, {
      'update': {
        method: 'PUT'
      }
    });
  };

  $scope.addWichtel = function() {
    if ($scope.eventRes === null) {
      $scope.title = 'Titel';
      $scope.eventsRes.save({
        name: $scope.title
      }, $scope.onEventSavedWichtel);
      return;
    }
    var wichtelTupl = {
      name: $scope.currentName,
      email: $scope.currentEmail
    };
    $scope.wichtelRes.save(wichtelTupl, function(data, headers) {
      var res = $resource(headers('Location'));
      res.get(function(data) {
        $scope.wichtels.push(data);
      });
    });
    $scope.currentName = '';
    $scope.currentEmail = '';
    document.getElementById('currentName').focus();
  };
  
  $scope.onEventSavedWichtel = function(data, headers) {
    $scope.eventRes = $resource(headers('Location'), null, {
      'update': {
        method: 'PUT'
      }
    });
    $scope.eventRes.get($scope.onEventReceivedWichtel);
  };

  $scope.onEventReceivedWichtel = function(data) {
    $scope.wichtelRes = $resource(data.links[0].href);
    $scope.completedRes = $resource(data.links[1].href, null, {
      'update': {
        method: 'PUT'
      }
    });
    $scope.addWichtel();
  };
  
  $scope.deleteWichtel = function(wichtel) {
    var $res = $resource(wichtel.links[0].href).delete(function(data) {
      var index = $scope.wichtels.indexOf(wichtel);
      $scope.wichtels.splice(index, 1);
    });
  };

  $scope.completeEvent = function() {
    $scope.sendingMails = true;
    $scope.triedToSend = true;
    $scope.completedRes.update($scope.onComplete, $scope.onCompleteFaliure);
  };

  $scope.onComplete = function(data, headers) {
    $scope.sendStatusRes = $resource(headers('Location'));
    $scope.onGetNewSendState(null);
  };

  $scope.onGetNewSendState = function(data) {
    var allSent = true;
    var states = null;
    if (data != null) {
      states = data.states;
    }
    for (var w = 0; w < $scope.wichtels.length; w++) {
      var wichtel = $scope.wichtels[w];
      if (states != null) {
        for (var d = 0; d < states.length; d++) {
          if (states[d].wichtelResId === wichtel.resId) {
            wichtel.mailSent = states[d].mailSent;
            wichtel.error = states[d].error;
            wichtel.sendError = states[d].sendError;
            break;
          }
        }
      }
      if (wichtel.mailSent === false) {
        allSent = false;
      }
    }
    if (states != null && allSent) {
      $scope.sendingMails = false;
    } else {
      window.setTimeout(function() {
        $scope.sendStatusRes.get($scope.onGetNewSendState);
      }, 1000);
    }
  };

  $scope.onCompleteFaliure = function(error) {
    $scope.sendingMails = false;
    alert('Ups, da ist was schief gegangen.\n' + 'Fehlermeldung:\n' + error.data.status + ' ' + error.data.error + '\n'
            + error.data.message);
  };
  
  $scope.showTime = function(wichtel) {
    return wichtel.mailSent == false;
  };
  $scope.showOK = function(wichtel) {
    return wichtel.mailSent == true && wichtel.error == false;
  };
  $scope.showError = function(wichtel) {
    return wichtel.mailSent == true && wichtel.error == true;
  };
  $scope.showErrorMessage = function(wichtel) {
    alert(wichtel.sendError);
  }
});

app.controller('LostAndFoundController', function($scope) {
  $scope.lostAndFound = function() {
    alert('Oh. Schön, dass dir dieses Feature gefällt. Leider funktioniert es im Moment noch nicht. Wir arbeiten aber daran.');
  }
});