const BASE_URL = 'http://localhost:8080/';
let clickMeBtn = document.getElementById('clickMe');

clickMeBtn.addEventListener('click', () => window.location.replace(BASE_URL + 'addCustomer'));

/*clickMeBtn.addEventListener('click', function () {
    window.location.replace(BASE_URL + 'addCustomer')
});*/

/*
clickMeBtn.addEventListener('click', function () {
    window.location.replace(BASE_URL + 'reservation/' + clickMeBtn.textContent);
});*/
