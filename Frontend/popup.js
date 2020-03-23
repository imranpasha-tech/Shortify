'use strict';

let originalUrl = document.getElementById("originalUrl");
chrome.storage.local.get(['originalUrl'], function(result) {
    console.log('Url value from storage is: ' + result.originalUrl);
    originalUrl.setAttribute('value', result.originalUrl);
});

//For copying the new url on button click.
var copyElement = document.getElementById('copy');
copyElement.addEventListener('click', function(event) {
  console.log("copy event invoked");
     var input  = document.getElementById("newUrl");
     event.preventDefault();
     input.select();
     document.execCommand("copy");

     try {
       // Now that we've selected the anchor text, execute the copy command
       var successful = document.execCommand('copy',true);
       var msg = successful ? 'successful' : 'unsuccessful';
         if(msg === "successful") {
           copyElement.classList.add('copied');
           copyElement.innerHTML = "Copied";
         }     
     } catch(err) {
       alert('Oops, unable to copy');
     }   
}
);

/**
 *  polpulate input value on clicking create button.
 */
var createElement = document.querySelector('#create');
createElement.addEventListener('click', function(event) {
    document.getElementsByClassName('new')[0].setAttribute("style", "display:block;");
    const longUrl = document.getElementById('originalUrl').value;
    document.getElementById('newUrl').value = "creating...";
    console.log("longUrl 1:", longUrl);
    let storageValue;

    chrome.storage.local.get(longUrl, function(result) {
      console.log('Value currently is ' + result[longUrl]);
      storageValue = result[longUrl];

      if(storageValue == undefined) {
        console.log(storageValue == null);
        shortify(longUrl);
      } else {
        document.getElementById('newUrl').value = storageValue;
        document.querySelector("#copy").setAttribute("style", "display:inline;");
        copyElement.dispatchEvent(new Event('click'));
      }
    });

  
});

/**
 * A call to backend to create short url on clicking create
 */
function shortify(longUrl) {
  const url = "https://lig4gyvvac.execute-api.ap-south-1.amazonaws.com/Shortify_v001/api";
  const data = {
    originalUrl: longUrl
  }
  const otherParams = {
    body: JSON.stringify(data),
    headers: {
      'Content-Type': 'application/json',  // This is very important othewise cors error will occur.
  },
    method: "POST"
  }

  fetch(url, otherParams)
    .then(response => {
      if(!response.ok) {
        throw Error(response.statusText);
      }
      return response.json();
    })
    .then(data => {
      console.log(data);
      document.getElementById('newUrl').value = data.shortenedUrl;
      document.querySelector("#copy").setAttribute("style", "display:inline;");
      copyElement.dispatchEvent(new Event('click'));
      var obj = {};
      obj[longUrl] = data.shortenedUrl;
      chrome.storage.local.set(obj, function() {
        console.log('url is set to ' + data.shortenedUrl);
      });

    })
    .catch(err => {
      console.log('Looks like there was a problem: \n', err);
    })
}

var feedback = document.querySelector('#feedback');
feedback.addEventListener('click', function(event) {
  console.log("Feedback event called");
  const footer = document.querySelector(".footer");
  footer.setAttribute("style", "display:none;");
  const rating = document.querySelector(".rating");
  rating.setAttribute("style", "display:block");
  rating.classList.add('footer');
});

var website = document.querySelector("#website");
website.addEventListener('click', function(event) {
  console.log("Website event called");
  const footer = document.querySelector(".footer");
  footer.setAttribute("style", "display:none;");
  const website = document.querySelector(".website");
  website.setAttribute("style", "display:block");
  website.classList.add('footer');
})

var ratings = document.querySelectorAll("span");

ratings.forEach(rating => {
    rating.addEventListener('click', function(event) {
    console.log(rating.innerHTML);
    //backend call to store rating.
  });
})
