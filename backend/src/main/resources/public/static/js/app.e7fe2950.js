(function(e){function t(t){for(var a,o,c=t[0],i=t[1],u=t[2],s=0,f=[];s<c.length;s++)o=c[s],Object.prototype.hasOwnProperty.call(r,o)&&r[o]&&f.push(r[o][0]),r[o]=0;for(a in i)Object.prototype.hasOwnProperty.call(i,a)&&(e[a]=i[a]);d&&d(t);while(f.length)f.shift()();return l.push.apply(l,u||[]),n()}function n(){for(var e,t=0;t<l.length;t++){for(var n=l[t],a=!0,o=1;o<n.length;o++){var i=n[o];0!==r[i]&&(a=!1)}a&&(l.splice(t--,1),e=c(c.s=n[0]))}return e}var a={},r={app:0},l=[];function o(e){return c.p+"static/js/"+({about:"about"}[e]||e)+"."+{about:"c3c107ee"}[e]+".js"}function c(t){if(a[t])return a[t].exports;var n=a[t]={i:t,l:!1,exports:{}};return e[t].call(n.exports,n,n.exports,c),n.l=!0,n.exports}c.e=function(e){var t=[],n=r[e];if(0!==n)if(n)t.push(n[2]);else{var a=new Promise((function(t,a){n=r[e]=[t,a]}));t.push(n[2]=a);var l,i=document.createElement("script");i.charset="utf-8",i.timeout=120,c.nc&&i.setAttribute("nonce",c.nc),i.src=o(e);var u=new Error;l=function(t){i.onerror=i.onload=null,clearTimeout(s);var n=r[e];if(0!==n){if(n){var a=t&&("load"===t.type?"missing":t.type),l=t&&t.target&&t.target.src;u.message="Loading chunk "+e+" failed.\n("+a+": "+l+")",u.name="ChunkLoadError",u.type=a,u.request=l,n[1](u)}r[e]=void 0}};var s=setTimeout((function(){l({type:"timeout",target:i})}),12e4);i.onerror=i.onload=l,document.head.appendChild(i)}return Promise.all(t)},c.m=e,c.c=a,c.d=function(e,t,n){c.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:n})},c.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},c.t=function(e,t){if(1&t&&(e=c(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var n=Object.create(null);if(c.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var a in e)c.d(n,a,function(t){return e[t]}.bind(null,a));return n},c.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return c.d(t,"a",t),t},c.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},c.p="/",c.oe=function(e){throw console.error(e),e};var i=window["webpackJsonp"]=window["webpackJsonp"]||[],u=i.push.bind(i);i.push=t,i=i.slice();for(var s=0;s<i.length;s++)t(i[s]);var d=u;l.push([0,"chunk-vendors"]),n()})({0:function(e,t,n){e.exports=n("56d7")},"16ce":function(e,t,n){e.exports=n.p+"static/img/fac.c3d8f587.gif"},"242e":function(e,t,n){"use strict";n("ec79")},"2bde":function(e,t,n){},"56d7":function(e,t,n){"use strict";n.r(t);n("e260"),n("e6cf"),n("cca6"),n("a79d");var a=n("7a23"),r=Object(a["f"])("div",{id:"nav"},null,-1);function l(e,t){var n=Object(a["v"])("router-view");return Object(a["o"])(),Object(a["d"])(a["a"],null,[r,Object(a["f"])(n)],64)}n("242e");const o={};o.render=l;var c=o,i=(n("d3b7"),n("3ca3"),n("ddb0"),n("6c02")),u={class:"home"};function s(e,t,n,r,l,o){var c=Object(a["v"])("HelloWorld");return Object(a["o"])(),Object(a["d"])("div",u,[Object(a["f"])(c)])}n("b0c0");var d=n("16ce"),f=n.n(d),g=n("bb58"),b=n.n(g),m=Object(a["C"])("data-v-d264f4ea");Object(a["r"])("data-v-d264f4ea");var h={class:"hello"},p=Object(a["f"])("div",{class:"overlay-image"},[Object(a["f"])("a",{href:"https://www.u-bordeaux.fr/",target:"_blank"},[Object(a["f"])("img",{class:"image",src:f.a,alt:"université bordeaux"}),Object(a["f"])("div",{class:"hover"},[Object(a["f"])("div",{class:"text"},"Site web de l'université de Bordeaux")])])],-1),v=Object(a["f"])("div",{class:"overlay-image2"},[Object(a["f"])("a",{href:"https://gitlab.emi.u-bordeaux.fr/cezahe/projet-pdl",target:"_blank"},[Object(a["f"])("img",{class:"image",src:b.a,alt:"projet gitlab"}),Object(a["f"])("div",{class:"hover"},[Object(a["f"])("div",{class:"text"},"Projet sur GitLab")])])],-1),y={class:"container"},j={class:"large-12 medium-12 small-12 cell"},O=Object(a["f"])("label",{for:"file",class:"button button1"},"Choisir une image",-1),w=Object(a["f"])("span",{id:"up"}," (Aucun fichier sélectionné) ",-1),A={class:"galery"},I=Object(a["f"])("img",{src:"",alt:"pas d'image chargée",id:"galleryCenter"},null,-1),x=Object(a["f"])("div",null,null,-1),C={class:"test"},E={class:"algorithms"},k={key:1,id:"gain",placeholder:"Gain"},F={key:2,id:"teinte",placeholder:"Teinte (comprise entre 0 et 360)",size:"30"},S={key:3,id:"filtersize",placeholder:"Niveau de flou"},T=Object(a["f"])("a",{class:"button button1",href:"",title:"DownloadImage",id:"download"},[Object(a["f"])("img",{alt:" Download Image"})],-1);Object(a["p"])();var B=m((function(e,t,n,r,l,o){return Object(a["o"])(),Object(a["d"])("div",h,[p,v,Object(a["f"])("div",y,[Object(a["f"])("div",j,[O,Object(a["f"])("input",{name:"uploadDocument",type:"file",id:"file",ref:"file",style:{display:"none"},onChange:t[1]||(t[1]=function(e){return o.handleFileUpload(),o.upload()})},null,544),w,Object(a["f"])("button",{class:"button button2",onClick:t[2]||(t[2]=function(e){return o.submitFile()})},"Submit")])]),Object(a["f"])("button",{class:"button button2",onClick:t[3]||(t[3]=function(e){return o.removeImage()})},"SUPPRIMER CETTE IMAGE NULLE"),Object(a["f"])("div",A,[Object(a["f"])("p",null,[Object(a["f"])("i",{class:"arrow right",onClick:t[4]||(t[4]=function(e){return o.right()})}),I,Object(a["f"])("i",{class:"arrow left",onClick:t[5]||(t[5]=function(e){return o.left()})})])]),x,Object(a["f"])("div",C,[(Object(a["o"])(!0),Object(a["d"])(a["a"],null,Object(a["u"])(l.allImages,(function(t,n){return Object(a["o"])(),Object(a["d"])("img",{class:"vignettes",value:n,key:t,src:t,alt:e.pout,onClick:function(e){return o.choose(n)}},null,8,["value","src","alt","onClick"])})),128))]),Object(a["f"])("div",E,[Object(a["B"])(Object(a["f"])("select",{"onUpdate:modelValue":t[6]||(t[6]=function(e){return l.currentAlgo=e}),alt:"Selectionner un algo"},[(Object(a["o"])(!0),Object(a["d"])(a["a"],null,Object(a["u"])(l.algos,(function(e,t){return Object(a["o"])(),Object(a["d"])("option",{value:t,key:e},Object(a["x"])(e.name),9,["value"])})),128))],512),[[a["z"],l.currentAlgo]]),3==l.currentAlgo?Object(a["B"])((Object(a["o"])(),Object(a["d"])("select",{key:0,"onUpdate:modelValue":t[7]||(t[7]=function(e){return l.filterType=e}),alt:"Selectionner un filtre"},[(Object(a["o"])(!0),Object(a["d"])(a["a"],null,Object(a["u"])(l.filtres,(function(e,t){return Object(a["o"])(),Object(a["d"])("option",{value:t,key:e},Object(a["x"])(e.text),9,["value"])})),128))],512)),[[a["z"],l.filterType]]):0==l.currentAlgo?(Object(a["o"])(),Object(a["d"])("input",k)):2==l.currentAlgo?(Object(a["o"])(),Object(a["d"])("input",F)):Object(a["e"])("",!0),3==l.currentAlgo?(Object(a["o"])(),Object(a["d"])("input",S)):Object(a["e"])("",!0)]),Object(a["f"])("button",{class:"button button2",onClick:t[8]||(t[8]=function(e){return o.algo(l.listImages[l.galleryActual].id)})},"Apply Algorithm"),T])})),U=n("b85c"),L=(n("1276"),n("ac1f"),n("c19f"),n("5cc6"),n("9a8c"),n("a975"),n("735e"),n("c1ac"),n("d139"),n("3a7b"),n("d5d6"),n("82f8"),n("e91f"),n("60bd"),n("5f96"),n("3280"),n("3fcc"),n("ca91"),n("25a1"),n("cd26"),n("3c5d"),n("2954"),n("649e"),n("219c"),n("170b"),n("b39a"),n("72f7"),n("bc3a")),P=n.n(L),R={name:"HelloWorld",data:function(){return{selected:"",listImages:[],galleryActual:0,file:"",dldImage:"blob",galeryImage:"blob",allImages:[],errors:[],pixel:5,algos:[{name:"Filtre de Luminosité",text:"increaseLuminosity",value:0},{name:"Egalisation d'Histograme",text:"histogram",value:1},{name:"Filtre Coloré",text:"coloredFilter",value:2},{name:"Filtre de Flou",text:"blurryFilter",value:3},{name:"Filtre de Bordure (ne marche pas)",text:"borderFilter",value:4}],currentAlgo:0,filtres:[{text:"gaussien",value:0},{text:"moyen",value:1}],filterType:0,displayedImage:0,newVign:0}},methods:{left:function(){this.galleryActual>0&&this.galleryActual--,this.choose(this.galleryActual)},right:function(){this.galleryActual<this.allImages.length-1&&this.galleryActual++,this.choose(this.galleryActual)},upload:function(){document.getElementById("up").innerHTML=this.file.name},choose:function(e){var t=this;this.galleryActual=e,P.a.get("images/"+e,{responseType:"blob"}).then((function(e){var n=new window.FileReader;n.readAsDataURL(e.data),n.onload=function(){document.getElementById("galleryCenter").setAttribute("src",n.result),document.getElementById("download").setAttribute("href",t.allImages[t.galleryActual]),document.getElementById("download").setAttribute("download",t.listImages[t.galleryActual].name)}}))},gallery:function(){var e=this;this.allImages=[];var t=[];function n(e){var t=this;return new Promise((function(n,a){try{P.a.get("images/"+e+"?vignette=true",{responseType:"blob"}).then((function(e){var t=new window.FileReader;t.readAsDataURL(e.data),t.onload=function(){n(t.result)}})).catch((function(e){alert(e),t.errors.push(e)}))}catch(r){a()}}))}var a,r=Object(U["a"])(this.listImages);try{for(r.s();!(a=r.n()).done;){var l=a.value;t.push(n(l.id))}}catch(o){r.e(o)}finally{r.f()}Promise.all(t).then((function(t){e.allImages=t})).catch((function(t){alert(t),e.errors.push(t)})).finally((function(){e.choose(e.galleryActual)}))},removeImage:function(){var e=this;P.a.delete("images/"+this.listImages[this.galleryActual].id,{data:{answer:42}}).then((function(){e.getImages(),e.galleryActual>0?e.galleryActual--:e.galleryActual<e.allImages.length&&e.galleryActual++,document.getElementById("galleryCenter").setAttribute("src",e.allImages[e.galleryActual])})).catch((function(t){alert(t),e.errors.push(t)}))},getImages:function(){var e=this;return P.a.get("images").then((function(t){e.listImages=t.data,e.gallery()})).catch((function(t){alert(t),e.errors.push(t)}))},algo:function(e){var t=this,n="?algorithm="+this.algos[this.currentAlgo].text;0==this.currentAlgo&&(n=n+"&gain="+document.getElementById("gain").value),3==this.currentAlgo&&(n=n+"&filter="+this.filtres[this.filterType].text+"&filtersize="+document.getElementById("filtersize").value),2==this.currentAlgo&&(n=n+"&teinte="+document.getElementById("teinte").value);var a=new Promise((function(a,r){try{P.a.get("/images/"+e+n,{responseType:"blob"}).then((function(e){var n=new window.FileReader;n.readAsDataURL(e.data),n.onload=function(){document.getElementById("galleryCenter").setAttribute("src",n.result);var e=document.createElement("img");e.src=n.result;var r=document.createElement("canvas"),l=r.getContext("2d");l.drawImage(e,0,0);var o=110,c=50,i=e.width,u=e.height;i>u?i>o&&(u*=o/i,i=o):u>c&&(i*=c/u,u=c),r.width=i,r.height=u,l=r.getContext("2d"),l.drawImage(e,0,0,i,u);var s=r.toDataURL("image/jpeg");t.allImages[t.galleryActual]=s,t.newVign=s,a(s)},t.getImages()})).catch((function(e){alert(e),t.errors.push(e)}))}catch(l){r()}}));a.then((function(e){function n(e){for(var t=atob(e.split(",")[1]),n=e.split(",")[0].split(":")[1].split(";")[0],a=new ArrayBuffer(t.length),r=new Uint8Array(a),l=0;l<t.length;l++)r[l]=t.charCodeAt(l);return new Blob([a],{type:n})}var a=new FormData;a.append("image",n(e)),alert(a.data),P.a.post("/images/"+t.galleryActual,a).then((function(){console.log("SUCCESS!!")})).catch((function(){console.log("FAILURE!!")}))})).catch((function(e){alert(e),t.errors.push(e)})).finally((function(){t.gallery()}))},downloadSelectedImage:function(e){var t=this;P.a.get("/images/"+e,{responseType:"blob"}).then((function(e){var t=new window.FileReader;t.readAsDataURL(e.data),t.onload=function(){document.getElementById("imagedld").setAttribute("src",t.result)}})).catch((function(e){t.errors.push(e)}))},handleFileUpload:function(){this.file=this.$refs.file.files[0]},submitFile:function(){var e=this,t=new FormData;t.append("file",this.file),P.a.post("/images",t,{headers:{"Content-Type":"multipart/form-data"}}).then((function(){e.getImages(),console.log("SUCCESS!!")})).catch((function(){console.log("FAILURE!!")}))}},mounted:function(){this.getImages()}};n("c771");R.render=B,R.__scopeId="data-v-d264f4ea";var D=R,_={name:"Home",components:{HelloWorld:D}};_.render=s;var z=_,H=[{path:"/",name:"Home",component:z},{path:"/about",name:"About",component:function(){return n.e("about").then(n.bind(null,"f820"))}}],M=Object(i["a"])({history:Object(i["b"])(),routes:H}),V=M;Object(a["c"])(c).use(V).mount("#app")},bb58:function(e,t,n){e.exports=n.p+"static/img/giphy.dca7cf24.gif"},c771:function(e,t,n){"use strict";n("2bde")},ec79:function(e,t,n){}});
//# sourceMappingURL=app.e7fe2950.js.map