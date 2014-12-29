angular
		.module('wichtelApp', [])
		.controller(
				'EventController',
				[
						'$scope',
						function($scope) {
							$scope.title = '';
							$scope.id = '';
							$scope.currentWichtel = '';
							$scope.wichtels = [];

							$scope.createEvent = function() {
								alert('Im Moment kann man noch keine Auslosungen vornehmen. Wir arbeiten daran...');
							};

							$scope.addWichtel = function() {
								$scope.wichtels.push({email : $scope.currentWichtel});
								$scope.currentWichtel = '';
							}
						} ]);