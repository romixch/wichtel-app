angular
		.module('wichtelApp', [])
		.controller(
				'EventController',
				[
						'$scope',
						function($scope) {
							$scope.title = '';
							$scope.id = '';
							$scope.currentName = '';
							$scope.currentEmail = '';
							$scope.wichtels = [];

							$scope.createEvent = function() {
								alert('Im Moment kann man noch keine Auslosungen vornehmen. Wir arbeiten daran...');
							};

							$scope.addWichtel = function() {
								$scope.wichtels.push({name : $scope.currentName, email : $scope.currentEmail});
								$scope.currentName = '';
								$scope.currentEmail = '';
								document.getElementById('currentName').focus();
							}
						} ]);