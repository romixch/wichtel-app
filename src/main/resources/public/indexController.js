var app = angular.module('wichtelApp', [ 'ngResource' ]);

app
		.controller(
				'EventController',
				function($scope, $resource) {
					$scope.title = '';
					$scope.currentName = '';
					$scope.currentEmail = '';
					$scope.wichtels = [];

					$scope.eventsRes = $resource("/rest/event");
					$scope.eventRes = null;
					$scope.wichtelRes = null;
					$scope.completedRes = null;

					$scope.blurTitle = function() {
						if ($scope.title === '') {
							return;
						}
						if ($scope.eventRes === null) {
							$scope.eventsRes.save({
								name : $scope.title
							}, $scope.onEventSaved);
						}
					};

					$scope.onEventSaved = function(data, headers) {
						$scope.eventRes = $resource(headers('Location'));
						$scope.eventRes.get($scope.onEventReceived);
					};

					$scope.onEventReceived = function(data) {
						$scope.wichtelRes = $resource(data.links[0].href);
						$scope.completedRes = $resource(data.links[1].href,
								null, {
									'update' : {
										method : 'PUT'
									}
								});
					};

					$scope.addWichtel = function() {
						var wichtelTupl = {
							name : $scope.currentName,
							email : $scope.currentEmail
						};
						$scope.wichtelRes.save(wichtelTupl);
						$scope.wichtels.push(wichtelTupl);
						$scope.currentName = '';
						$scope.currentEmail = '';
						document.getElementById('currentName').focus();
					};

					$scope.completeEvent = function() {
						$scope.completedRes
								.update(
										'true',
										function(data) {
											alert('Vielen Dank für dein Interesse. Der Versand der E-Mails ist noch nicht automatisiert. Er kann daher eine Weile dauern.');
										});
					};
				});

app.controller('LostAndFoundController', function($scope) {
	$scope.lostAndFound = function() {
		alert('Oh. Schön, dass dir dieses Feature gefällt. Leider funktioniert es im Moment noch nicht. Wir arbeiten aber daran.');
	}
});