'use strict';

let originalUrl = document.getElementById("originalUrl");
chrome.storage.local.get(['originalUrl'], function(result) {
    console.log('Url value from storage is: ' + result.originalUrl);
    originalUrl.setAttribute('value', result.originalUrl);
});



//For copying the new url on button click.
var copyElement = document.getElementById('copy');
copyElement.addEventListener('click', function(event) {
    let customUrlLink = document.querySelector('.newUrl');
    let range = document.createRange();
    range.selectNode(customUrlLink);
    window.getSelection().addRange(range);

    try {
        // Now that we've selected the anchor text, execute the copy command
        var successful = document.execCommand('copy');
        var msg = successful ? 'successful' : 'unsuccessful';
          if(msg === "successful") {
            copyElement.classList.add('copied');
            copyElement.innerHTML = "Copied";
          }     
      } catch(err) {
        alert('Oops, unable to copy');
      }
});

/**
 *  polpulate input value on clicking create button.
 */
var createElement = document.querySelector('#create');
createElement.addEventListener('click', function(event) {
    document.getElementsByClassName('new')[0].setAttribute("style", "display:block;");
    document.getElementById('newUrl').value = "creating...";
    //document.querySelector('#newUrl').value = "creating...";
    setTimeout(delayUrl, 2000);  
});

function delayUrl() {
  console.log("delayUrl called");
  // fetch('https://jsonplaceholder.typicode.com/todos/1')
  //   .then(response => response.json())
  //   .then(json => document.getElementById('newUrl').value = json.userId);

  //let newUrl = document.getElementById('newUrl');
  chrome.storage.local.get(['shortifyUrl'], function(result) {
    console.log('shortifyUrl value from storage is: ' + result.shortifyUrl);
    //newUrl.setAttribute('value', result.shortifyUrl); //why setAttribute is not replaced old value?
    document.getElementById('newUrl').value = result.shortifyUrl;
    });
  document.querySelector("#copy").setAttribute("style", "display:inline;");
}
