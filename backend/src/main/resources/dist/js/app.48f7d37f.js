(function(e){function t(t){for(var r,i,u=t[0],c=t[1],s=t[2],f=0,d=[];f<u.length;f++)i=u[f],Object.prototype.hasOwnProperty.call(o,i)&&o[i]&&d.push(o[i][0]),o[i]=0;for(r in c)Object.prototype.hasOwnProperty.call(c,r)&&(e[r]=c[r]);l&&l(t);while(d.length)d.shift()();return a.push.apply(a,s||[]),n()}function n(){for(var e,t=0;t<a.length;t++){for(var n=a[t],r=!0,u=1;u<n.length;u++){var c=n[u];0!==o[c]&&(r=!1)}r&&(a.splice(t--,1),e=i(i.s=n[0]))}return e}var r={},o={app:0},a=[];function i(t){if(r[t])return r[t].exports;var n=r[t]={i:t,l:!1,exports:{}};return e[t].call(n.exports,n,n.exports,i),n.l=!0,n.exports}i.m=e,i.c=r,i.d=function(e,t,n){i.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:n})},i.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},i.t=function(e,t){if(1&t&&(e=i(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var n=Object.create(null);if(i.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var r in e)i.d(n,r,function(t){return e[t]}.bind(null,r));return n},i.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return i.d(t,"a",t),t},i.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},i.p="/";var u=window["webpackJsonp"]=window["webpackJsonp"]||[],c=u.push.bind(u);u.push=t,u=u.slice();for(var s=0;s<u.length;s++)t(u[s]);var l=c;a.push([0,"chunk-vendors"]),n()})({0:function(e,t,n){e.exports=n("56d7")},"034f":function(e,t,n){"use strict";n("85ec")},4749:function(e,t,n){},"56d7":function(e,t,n){"use strict";n.r(t);n("e260"),n("e6cf"),n("cca6"),n("a79d");var r=n("2b0e"),o=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{attrs:{id:"app"}},[n("div",{attrs:{id:"app-wrap"}},[n("navBar",{attrs:{id:"navBar"}}),n("router-view"),n("Footer",{attrs:{id:"footer"}})],1)])},a=[],i=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",[r("nav",{attrs:{id:"nav"}},[r("div",{attrs:{id:"logo"}},[r("router-link",{attrs:{to:"/main"}},[r("img",{attrs:{src:n("f08a"),alt:""}})])],1)])])},u=[],c={name:"navBar"},s=c,l=(n("b3db"),n("2877")),f=Object(l["a"])(s,i,u,!1,null,"7b50adea",null),d=f.exports,p=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("footer",[n("div",{attrs:{id:"flex-container"}},[n("router-link",{attrs:{to:"/main"}},[e._v("home")]),n("router-link",{attrs:{to:"/feed"}},[e._v("menu")]),n("router-link",{attrs:{to:"/user/mileage"}},[e._v("savings")]),n("router-link",{attrs:{to:"/user/profile/"+e.nickname}},[e._v("person")])],1)])},v=[],b={name:"Footer"},g=b,h=(n("5d8b"),Object(l["a"])(g,p,v,!1,null,"fb8e9fec",null)),m=h.exports,w={name:"App",components:{navBar:d,Footer:m}},y=w,_=(n("034f"),Object(l["a"])(y,o,a,!1,null,null,null)),O=_.exports,j=n("9483");Object(j["a"])("".concat("/","service-worker.js"),{ready:function(){console.log("App is being served from cache by a service worker.\nFor more details, visit https://goo.gl/AFskqB")},registered:function(){console.log("Service worker has been registered.")},cached:function(){console.log("Content has been cached for offline use.")},updatefound:function(){console.log("New content is downloading.")},updated:function(){console.log("New content is available; please refresh.")},offline:function(){console.log("No internet connection found. App is running in offline mode.")},error:function(e){console.error("Error during service worker registration:",e)}});var k=n("8c4f");r["default"].use(k["a"]);var x=[],P=new k["a"]({mode:"history",base:"/",routes:x}),S=P,B=n("2f62");r["default"].use(B["a"]);var F=new B["a"].Store({state:{},mutations:{},actions:{},modules:{}}),A=n("5f5b"),E=n("b1e0");n("2dd8");r["default"].use(A["a"]),r["default"].use(E["a"]),r["default"].config.productionTip=!1,new r["default"]({router:S,store:F,render:function(e){return e(O)}}).$mount("#app")},"5d8b":function(e,t,n){"use strict";n("4749")},"85ec":function(e,t,n){},a9ce:function(e,t,n){},b3db:function(e,t,n){"use strict";n("a9ce")},f08a:function(e,t,n){e.exports=n.p+"img/logo5-2.6532b1e9.png"}});
//# sourceMappingURL=app.48f7d37f.js.map