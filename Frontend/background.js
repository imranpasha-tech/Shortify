//The URL is stored at the events of new tab creation, activation of current tab and
//refreshing(updating) current tab.

'use strict';

chrome.tabs.onUpdated.addListener(updateURL);
chrome.tabs.onActivated.addListener(activateURL);
chrome.tabs.onCreated.addListener(createURL);

//Fetch tab url and persist in storage
function updateURL(tabId, changeInfo, tab) {
    if(changeInfo.status !== "complete")
        return;
    console.log("tab url ", tab.url);
    storeUrls(tab.url);
}

//Fetch activated tab url and persist in storage
function activateURL(activeInfo) {
    console.log("activated callback", activeInfo);
    chrome.tabs.query({currentWindow: true, active: true}, function(tabs){
        console.log(tabs[0].url);
        storeUrls(tabs[0].url);
    });
}

function createURL(tab) {
    console.log("onCreate Callback:", tab);
    chrome.tabs.onUpdated.addListener(updateURL);
}

function storeUrls(newUrl) {
    chrome.storage.local.set({originalUrl: newUrl}, function() {
        console.log('url is set to ' + newUrl);
      });
    chrome.storage.local.get(['originalUrl'], function(result) {
        console.log('Url value from storage is: ' + result.originalUrl);
    });
 }
 
