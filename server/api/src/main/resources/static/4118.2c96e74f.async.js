(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[4118],{74118:function(We,_,u){"use strict";u.d(_,{cM:function(){return W},Bn:function(){return $},ZP:function(){return Oe}});var Xe=u(22385),g=u(94199),A=u(94657),ee=u(20310),h=u(69610),S=u(54941),D=u(63543),T=u(94663),x=u(43028),y=u(5869),te=u(94184),le=u.n(te),L=u(28579),Ve=u(44823),f=u(67294),m=u(71167),ne=u.n(m),oe=u(81067),F=u(11849),$e=u(71194),ie=u(50146),Ye=u(49111),re=u(19650),Ge=u(57663),O=u(71577),ae=u(93224),se=u(76095),q=u.n(se),ue=u(66913),de=u.n(ue),ce={modules:["DisplaySize","Toolbar","Resize"],overlayStyles:{position:"absolute",boxSizing:"border-box",border:"1px dashed #444"},handleStyles:{position:"absolute",height:"12px",width:"12px",backgroundColor:"white",border:"1px solid #777",boxSizing:"border-box",opacity:"0.80"},displayStyles:{position:"absolute",font:"12px/1.0 Arial, Helvetica, sans-serif",padding:"4px 8px",textAlign:"center",backgroundColor:"white",color:"#333",border:"1px solid #777",boxSizing:"border-box",opacity:"0.80",cursor:"default"},toolbarStyles:{position:"absolute",top:"-12px",right:"0",left:"0",height:"0",minWidth:"100px",font:"12px/1.0 Arial, Helvetica, sans-serif",textAlign:"center",color:"#333",boxSizing:"border-box",cursor:"default"},toolbarButtonStyles:{display:"inline-block",width:"24px",height:"24px",background:"white",border:"1px solid #999",verticalAlign:"middle"},toolbarButtonSvgStyles:{fill:"#444",stroke:"#444",strokeWidth:"2"}},N=function a(n){(0,h.Z)(this,a),this.onCreate=function(){},this.onDestroy=function(){},this.onUpdate=function(){},this.overlay=n.overlay,this.img=n.img,this.options=n.options,this.requestUpdate=n.onUpdate},be=function(a){(0,x.Z)(t,a);var n=(0,y.Z)(t);function t(){var e;(0,h.Z)(this,t);for(var r=arguments.length,l=new Array(r),i=0;i<r;i++)l[i]=arguments[i];return e=n.call.apply(n,[this].concat(l)),e.onCreate=function(){e.display=document.createElement("div"),Object.assign(e.display.style,e.options.displayStyles),e.overlay.appendChild(e.display)},e.onDestroy=function(){},e.onUpdate=function(){if(!(!e.display||!e.img)){var o=e.getCurrentSize();if(e.display.innerHTML=o.join(" &times; "),o[0]>120&&o[1]>30)Object.assign(e.display.style,{right:"4px",bottom:"4px",left:"auto"});else if(e.img.style.float=="right"){var s=e.display.getBoundingClientRect();Object.assign(e.display.style,{right:"auto",bottom:"-".concat(s.height+4,"px"),left:"-".concat(s.width+4,"px")})}else{var c=e.display.getBoundingClientRect();Object.assign(e.display.style,{right:"-".concat(c.width+4,"px"),bottom:"-".concat(c.height+4,"px"),left:"auto"})}}},e.getCurrentSize=function(){return[e.img.width,Math.round(e.img.width/e.img.naturalWidth*e.img.naturalHeight)]},e}return t}(N),pe=u(20668),fe=u.n(pe),he=u(15924),me=u.n(he),qe=u(43203),ve=u.n(qe),ge=u(76095),U=ge.imports.parchment,k=new U.Attributor.Style("float","float"),z=new U.Attributor.Style("margin","margin"),w=new U.Attributor.Style("display","display"),xe=function(a){(0,x.Z)(t,a);var n=(0,y.Z)(t);function t(){var e;(0,h.Z)(this,t);for(var r=arguments.length,l=new Array(r),i=0;i<r;i++)l[i]=arguments[i];return e=n.call.apply(n,[this].concat(l)),e.onCreate=function(){e.toolbar=document.createElement("div"),Object.assign(e.toolbar.style,e.options.toolbarStyles),e.overlay.appendChild(e.toolbar),e._defineAlignments(),e._addToolbarButtons()},e.onDestroy=function(){},e.onUpdate=function(){},e._defineAlignments=function(){e.alignments=[{icon:fe(),apply:function(){w.add(e.img,"inline"),k.add(e.img,"left"),z.add(e.img,"0 1em 1em 0")},isApplied:function(){return k.value(e.img)=="left"}},{icon:me(),apply:function(){w.add(e.img,"block"),k.remove(e.img),z.add(e.img,"auto")},isApplied:function(){return z.value(e.img)=="auto"}},{icon:ve(),apply:function(){w.add(e.img,"inline"),k.add(e.img,"right"),z.add(e.img,"0 0 1em 1em")},isApplied:function(){return k.value(e.img)=="right"}}]},e._addToolbarButtons=function(){var o=[];e.alignments.forEach(function(s,c){var b=document.createElement("span");o.push(b),b.innerHTML=s.icon,b.addEventListener("click",function(){o.forEach(function(C){return C.style.filter=""}),s.isApplied()?(k.remove(e.img),z.remove(e.img),w.remove(e.img)):(e._selectButton(b),s.apply()),e.requestUpdate()}),Object.assign(b.style,e.options.toolbarButtonStyles),c>0&&(b.style.borderLeftWidth="0"),Object.assign(b.children[0].style,e.options.toolbarButtonSvgStyles),s.isApplied()&&e._selectButton(b),e.toolbar.appendChild(b)})},e._selectButton=function(o){o.style.filter="invert(20%)"},e}return t}(N),ye=function(a){(0,x.Z)(t,a);var n=(0,y.Z)(t);function t(){var e;(0,h.Z)(this,t);for(var r=arguments.length,l=new Array(r),i=0;i<r;i++)l[i]=arguments[i];return e=n.call.apply(n,[this].concat(l)),e.onCreate=function(){e.boxes=[],e.addBox("nwse-resize"),e.addBox("nesw-resize"),e.addBox("nwse-resize"),e.addBox("nesw-resize"),e.positionBoxes()},e.onDestroy=function(){e.setCursor("")},e.positionBoxes=function(){var o="".concat(-parseFloat(e.options.handleStyles.width)/2,"px"),s="".concat(-parseFloat(e.options.handleStyles.height)/2,"px");[{left:o,top:s},{right:o,top:s},{right:o,bottom:s},{left:o,bottom:s}].forEach(function(c,b){Object.assign(e.boxes[b].style,c)})},e.addBox=function(o){var s=document.createElement("div");Object.assign(s.style,e.options.handleStyles),s.style.cursor=o,s.style.width="".concat(e.options.handleStyles.width,"px"),s.style.height="".concat(e.options.handleStyles.height,"px"),s.addEventListener("mousedown",e.handleMousedown,!1),e.overlay.appendChild(s),e.boxes.push(s)},e.handleMousedown=function(o){e.dragBox=o.target,e.dragStartX=o.clientX,e.preDragWidth=e.img.width||e.img.naturalWidth,e.setCursor(e.dragBox.style.cursor),document.addEventListener("mousemove",e.handleDrag,!1),document.addEventListener("mouseup",e.handleMouseup,!1)},e.handleMouseup=function(){e.setCursor(""),document.removeEventListener("mousemove",e.handleDrag),document.removeEventListener("mouseup",e.handleMouseup)},e.handleDrag=function(o){if(!!e.img){var s=o.clientX-e.dragStartX;e.dragBox===e.boxes[0]||e.dragBox===e.boxes[3]?e.img.width=Math.round(e.preDragWidth-s):e.img.width=Math.round(e.preDragWidth+s),e.requestUpdate()}},e.setCursor=function(o){[document.body,e.img].forEach(function(s){s.style.cursor=o})},e}return t}(N),R=u(76095),ke={DisplaySize:be,Toolbar:xe,Resize:ye},P=function a(n){var t=this,e=arguments.length>1&&arguments[1]!==void 0?arguments[1]:{};(0,h.Z)(this,a),this.initializeModules=function(){t.removeModules(),t.modules=t.moduleClasses.map(function(l){return new(ke[l]||l)(t)}),t.modules.forEach(function(l){l.onCreate()}),t.onUpdate()},this.onUpdate=function(){t.repositionElements(),t.modules.forEach(function(l){l.onUpdate()})},this.removeModules=function(){t.modules.forEach(function(l){l.onDestroy()}),t.modules=[]},this.handleClick=function(l){if(l.target&&l.target.tagName&&l.target.tagName.toUpperCase()==="IMG"){if(t.img===l.target)return;t.img&&t.hide(),t.show(l.target)}else t.img&&t.hide()},this.show=function(l){t.img=l,t.showOverlay(),t.initializeModules()},this.showOverlay=function(){t.overlay&&t.hideOverlay(),t.quill.setSelection(null),t.setUserSelect("none"),document.addEventListener("keyup",t.checkImage,!0),t.quill.root.addEventListener("input",t.checkImage,!0),t.overlay=document.createElement("div"),Object.assign(t.overlay.style,t.options.overlayStyles),t.quill.root.parentNode.appendChild(t.overlay),t.repositionElements()},this.hideOverlay=function(){!t.overlay||(t.quill.root.parentNode.removeChild(t.overlay),t.overlay=void 0,document.removeEventListener("keyup",t.checkImage),t.quill.root.removeEventListener("input",t.checkImage),t.setUserSelect(""))},this.repositionElements=function(){if(!(!t.overlay||!t.img)){var l=t.quill.root.parentNode,i=t.img.getBoundingClientRect(),o=l.getBoundingClientRect();Object.assign(t.overlay.style,{left:"".concat(i.left-o.left-1+l.scrollLeft,"px"),top:"".concat(i.top-o.top+l.scrollTop,"px"),width:"".concat(i.width,"px"),height:"".concat(i.height,"px")})}},this.hide=function(){t.hideOverlay(),t.removeModules(),t.img=void 0},this.setUserSelect=function(l){["userSelect","mozUserSelect","webkitUserSelect","msUserSelect"].forEach(function(i){t.quill.root.style[i]=l,document.documentElement.style[i]=l})},this.checkImage=function(l){t.img&&((l.keyCode==46||l.keyCode==8)&&R.find(t.img).deleteAt(0),t.hide())},this.quill=n;var r=!1;e.modules&&(r=e.modules.slice()),this.options=de()({},e,ce),r!==!1&&(this.options.modules=r),document.execCommand("enableObjectResizing",!1,"false"),this.quill.root.addEventListener("click",this.handleClick,!1),this.quill.root.parentNode.style.position=this.quill.root.parentNode.style.position||"relative",this.moduleClasses=this.options.modules,this.modules=[]};R&&R.register("modules/imageResize",P);var Ce=q().import("blots/block"),E=function(a){(0,x.Z)(t,a);var n=(0,y.Z)(t);function t(){return(0,h.Z)(this,t),n.apply(this,arguments)}return(0,S.Z)(t,[{key:"deleteAt",value:function(r,l){(0,D.Z)((0,T.Z)(t.prototype),"deleteAt",this).call(this,r,l),this.cache={}}}],[{key:"create",value:function(r){var l=(0,D.Z)((0,T.Z)(t),"create",this).call(this,r);if(r===!0)return l;var i=document.createElement("img");return i.setAttribute("src",r),l.appendChild(i),l}},{key:"value",value:function(r){var l=r.dataset,i=l.src,o=l.custom;return{src:i,custom:o}}}]),t}(Ce);E.blotName="imageBlot",E.className="image-uploading",E.tagName="span",q().register({"formats/imageBlot":E});var Se=E,Q=function(){function a(n,t){(0,h.Z)(this,a),this.quill=n,this.options=t,this.range=null,typeof this.options.upload!="function"&&console.warn("[Missing config] upload function that returns a promise is required");var e=this.quill.getModule("toolbar");e.addHandler("image",this.selectLocalImage.bind(this)),this.handleDrop=this.handleDrop.bind(this),this.handlePaste=this.handlePaste.bind(this),this.quill.root.addEventListener("drop",this.handleDrop,!1),this.quill.root.addEventListener("paste",this.handlePaste,!1)}return(0,S.Z)(a,[{key:"selectLocalImage",value:function(){var t=this;this.range=this.quill.getSelection(),this.fileHolder=document.createElement("input"),this.fileHolder.setAttribute("type","file"),this.fileHolder.setAttribute("accept","image/*"),this.fileHolder.setAttribute("style","visibility:hidden"),this.fileHolder.onchange=this.fileChanged.bind(this),document.body.appendChild(this.fileHolder),this.fileHolder.click(),window.requestAnimationFrame(function(){document.body.removeChild(t.fileHolder)})}},{key:"handleDrop",value:function(t){var e=this;if(t.stopPropagation(),t.preventDefault(),t.dataTransfer&&t.dataTransfer.files&&t.dataTransfer.files.length){if(document.caretRangeFromPoint){var r=document.getSelection(),l=document.caretRangeFromPoint(t.clientX,t.clientY);r&&l&&r.setBaseAndExtent(l.startContainer,l.startOffset,l.startContainer,l.startOffset)}else{var i=document.getSelection(),o=document.caretPositionFromPoint(t.clientX,t.clientY);i&&o&&i.setBaseAndExtent(o.offsetNode,o.offset,o.offsetNode,o.offset)}this.range=this.quill.getSelection();var s=t.dataTransfer.files[0];setTimeout(function(){e.range=e.quill.getSelection(),e.readAndUploadFile(s)},0)}}},{key:"handlePaste",value:function(t){var e=this,r=t.clipboardData||window.clipboardData;if(r&&(r.items||r.files))for(var l=r.items||r.files,i=/^image\/(jpe?g|gif|png|svg|webp)$/i,o=0;o<l.length;o++)i.test(l[o].type)&&function(){var s=l[o].getAsFile?l[o].getAsFile():l[o];s&&(e.range=e.quill.getSelection(),t.preventDefault(),setTimeout(function(){e.range=e.quill.getSelection(),e.readAndUploadFile(s)},0))}()}},{key:"readAndUploadFile",value:function(t){var e=this,r=!1,l=new FileReader;l.addEventListener("load",function(){if(!r){var i=l.result;e.insertBase64Image(i)}},!1),t&&l.readAsDataURL(t),this.options.upload(t).then(function(i){e.insertToEditor(i)},function(i){r=!0,e.removeBase64Image(),console.warn(i)})}},{key:"fileChanged",value:function(){var t=this.fileHolder.files[0];this.readAndUploadFile(t)}},{key:"insertBase64Image",value:function(t){var e=this.range;this.quill.insertEmbed(e.index,Se.blotName,"".concat(t),"user")}},{key:"insertToEditor",value:function(t){var e=this.range;this.quill.deleteText(e.index,3,"user"),this.quill.insertEmbed(e.index,"image","".concat(t)),e.index++,this.quill.setSelection(e,"user")}},{key:"removeBase64Image",value:function(){var t=this.range;this.quill.deleteText(t.index,3,"user")}}]),a}();window.ImageUploader=Q;var ze=Q,Ee=function(){function a(n,t){(0,h.Z)(this,a),this.quill=n,this.options=t,this.container=document.querySelector(t.container),n.on("text-change",this.update.bind(this)),this.update()}return(0,S.Z)(a,[{key:"calculate",value:function(){var t=this.quill.getText();return this.options.unit==="word"?(t=t.trim(),t.length>0?t.split(/\s+/).length:0):t.length}},{key:"update",value:function(){var t=this.calculate(),e=this.options.unit;t!==1&&(e+="s")}}]),a}();m.Quill.register("modules/counter",Ee);var we=u(3980),d=u(85893);q().register("modules/imageResize",P),q().register("modules/imageUploader",ze);var H=q().import("attributors/style/size");H.whitelist=["9px","10px","11px","12px","14px","16px","18px","20px","22px","24px","26px","36px"],q().register(H,!0);var Ze=function(n){var t=(0,f.useRef)(null),e=(0,f.useRef)(),r=(0,f.useRef)();return(0,f.useEffect)(function(){return t.current&&(e.current=new(q())(t.current,{theme:"snow",formats:["size","color","width","background","header","bold","italic","underline","strike","blockquote","list","bullet","indent","link","image","video","formula","align","imageBlot","code-block"],modules:{syntax:{highlight:function(i){return L.Z.highlightAuto(i).value}},counter:{container:"#counter",unit:"word"},imageResize:{parchment:q().import("parchment"),modules:["Resize","DisplaySize"]},imageUploader:{upload:function(i){return new Promise(function(o,s){we.hi.uploadImage(3,i).then(function(c){c.success&&o("/api/public/preview/".concat(c.data.id))}).catch(function(c){s("Upload failed"),console.error("Error:",c)})})}},toolbar:[[{size:["9px","10px","11px","12px","14px","16px","18px","20px","22px","24px","26px","36px"]}],[{color:[]},{background:[]}],["bold","italic","underline","strike","blockquote"],[{align:""},{align:"center"},{align:"right"},{align:"justify"}],["link","image","video","formula","code-block"],[{list:"ordered"},{list:"bullet"},{indent:"-1"},{indent:"+1"}]]}}),n.value&&e.current.setText(n.value),e.current.on("text-change",function(l,i,o){var s,c=(s=e.current)===null||s===void 0?void 0:s.root.innerHTML;c!==r.current&&n.onChange&&(n.onChange(c||""),r.current=c)})),function(){}},[]),(0,f.useEffect)(function(){var l,i=r.current,o=(l=n.value)!==null&&l!==void 0?l:"";if(o!==i){var s;r.current=o,(s=e.current)===null||s===void 0||s.setContents(e.current.clipboard.convert(o))}},[n.value]),(0,d.jsx)("div",{className:"custom-quill-editor",children:(0,d.jsx)("div",{ref:t})})},W=function(n){var t=n.onChange,e=n.onClose,r=n.value,l=(0,ae.Z)(n,["onChange","onClose","value"]),i=(0,f.useState)(r),o=(0,A.Z)(i,2),s=o[0],c=o[1];return(0,d.jsx)(ie.Z,(0,F.Z)((0,F.Z)({visible:!0,onCancel:e,footer:!1,maskClosable:!1,closable:!1,width:650},l),{},{children:(0,d.jsxs)("div",{children:[(0,d.jsx)(Ze,{value:s,onChange:function(C){c(C)}}),(0,d.jsx)("div",{style:{textAlign:"right",marginTop:20},children:(0,d.jsxs)(re.Z,{children:[(0,d.jsx)(O.Z,{onClick:e,children:"\u53D6\u6D88"}),(0,d.jsx)(O.Z,{type:"primary",onClick:function(){return t(s)},children:"\u4FDD\u5B58"})]})})]})}))},je=u(8166),Be=m.Quill.import("themes/bubble"),Ae=function(a){(0,x.Z)(t,a);var n=(0,y.Z)(t);function t(e,r){var l;return(0,h.Z)(this,t),l=n.call(this,e,r),e.on("selection-change",function(i){i&&(e.theme.tooltip.show(),e.theme.tooltip.position((0,F.Z)({},e.getBounds(i))))}),e.on("text-change",function(i){e.hasFocus()&&e.theme.tooltip.show()}),l}return t}(Be);m.Quill.register("themes/bubble",Ae);var De=u(87998),Te=q().import("modules/clipboard"),Fe=q().import("delta"),Ne=function(a){(0,x.Z)(t,a);var n=(0,y.Z)(t);function t(){return(0,h.Z)(this,t),n.apply(this,arguments)}return(0,S.Z)(t,[{key:"onPaste",value:function(r){r.preventDefault();var l=this.quill.getSelection(),i=r.clipboardData.getData("text/plain"),o=new Fe().retain(l.index).delete(l.length).insert(i),s=i.length+l.index,c=0;this.quill.updateContents(o,"silent"),this.quill.setSelection(s,c,"silent"),this.quill.scrollIntoView()}}]),t}(Te),Ue=Ne,X,Re=m.Quill.import("blots/embed");window.katex=je.Z;var V=m.Quill.import("attributors/style/size");V.whitelist=["9px","10px","11px","12px","14px","16px","18px","20px","22px","24px","26px","36px"],m.Quill.register(V,!0),m.Quill.register("modules/clipboard",Ue,!0);var Je=m.Quill.import("delta"),Z=function(a){(0,x.Z)(t,a);var n=(0,y.Z)(t);function t(){return(0,h.Z)(this,t),n.apply(this,arguments)}return(0,S.Z)(t,null,[{key:"create",value:function(r){var l=(0,D.Z)((0,T.Z)(t),"create",this).call(this);return l.setAttribute("contenteditable","true"),l.classList.add("myspan"),this._addRemovalButton(l),l}},{key:"_addRemovalButton",value:function(r){var l=document.createElement("span");l.innerText="___",l.contentEditable="true",r.appendChild(l)}},{key:"value",value:function(r){return r.childNodes[1].textContent}}]),t}(Re);Z.blotName="spanEmbed",Z.tagName="span",Z.className="myclass",m.Quill.register(Z);var Me=oe.ZP.div(X||(X=(0,ee.Z)([`
  box-sizing: border-box;
  margin: 0;
  font-variant: tabular-nums;
  list-style: none;
  font-feature-settings: 'tnum';
  width: 100%;
  padding: `,`;
  color: rgba(0, 0, 0, 0.65);

  border: `,`;
  transition: all 0.3s;

  &.is-focus {
    border: `,`;
    border-right-width: 1px !important;
    outline: 0;
  }
  &.not-focus:hover {
    border: dashed 1px #aaaaaa !important;
    border-right-width: 1px !important;
  }

  .ql-editor {
    padding: 0;
    font-size: `,`;
    text-align: `,`;
  }

  .ql-tooltip {
    top: -40px !important;
    left: -10px !important;
    width: 240px;
    transform: none;
  }

  .ql-formats > span,
  .ql-formats > button {
    &:hover {
      background-color: hsla(0, 0%, 84.7%, 0.3);
    }
  }

  .ql-bubble .ql-tooltip {
    z-index: 999;
    box-sizing: border-box;
    padding: 0 2px;
    /* white-space: nowrap; */
    background-color: #fff;
    /* border: 1px solid #eaeaea; */
    /* border-radius: 2px; rgb(0 0 0 / 20%); */
    border-radius: 2px;
    box-shadow: 0 1px 3px 0 rgb(0 0 0 / 20%);
    cursor: pointer;
  }

  .ql-bubble .ql-tooltip.ql-flip .ql-tooltip-arrow {
    display: none;
  }

  .ql-tooltip-arrow {
    display: none;
  }

  .ql-toolbar {
    box-sizing: border-box;
    font-family: 'Helvetica Neue', 'Helvetica', 'Arial', sans-serif;
    /* border: 1px solid #ccc; */
  }

  .ql-bubble .ql-picker {
    position: relative;
    display: inline-block;
    float: left;
    height: 24px;
    color: #444;
    font-weight: 500;
    font-size: 14px;
    vertical-align: middle;
  }

  .ql-bubble .ql-stroke {
    fill: none;
    stroke: #444;
    stroke-linecap: round;
    stroke-linejoin: round;
    stroke-width: 2;
  }

  .ql-bubble.ql-toolbar button:hover .ql-stroke,
  .ql-bubble .ql-toolbar button:hover .ql-stroke,
  .ql-bubble.ql-toolbar button:focus .ql-stroke,
  .ql-bubble .ql-toolbar button:focus .ql-stroke,
  .ql-bubble.ql-toolbar button.ql-active .ql-stroke,
  .ql-bubble .ql-toolbar button.ql-active .ql-stroke,
  .ql-bubble.ql-toolbar .ql-picker-label:hover .ql-stroke,
  .ql-bubble .ql-toolbar .ql-picker-label:hover .ql-stroke,
  .ql-bubble.ql-toolbar .ql-picker-label.ql-active .ql-stroke,
  .ql-bubble .ql-toolbar .ql-picker-label.ql-active .ql-stroke,
  .ql-bubble.ql-toolbar .ql-picker-item:hover .ql-stroke,
  .ql-bubble .ql-toolbar .ql-picker-item:hover .ql-stroke,
  .ql-bubble.ql-toolbar .ql-picker-item.ql-selected .ql-stroke,
  .ql-bubble .ql-toolbar .ql-picker-item.ql-selected .ql-stroke,
  .ql-bubble.ql-toolbar button:hover .ql-stroke-miter,
  .ql-bubble .ql-toolbar button:hover .ql-stroke-miter,
  .ql-bubble.ql-toolbar button:focus .ql-stroke-miter,
  .ql-bubble .ql-toolbar button:focus .ql-stroke-miter,
  .ql-bubble.ql-toolbar button.ql-active .ql-stroke-miter,
  .ql-bubble .ql-toolbar button.ql-active .ql-stroke-miter,
  .ql-bubble.ql-toolbar .ql-picker-label:hover .ql-stroke-miter,
  .ql-bubble .ql-toolbar .ql-picker-label:hover .ql-stroke-miter,
  .ql-bubble.ql-toolbar .ql-picker-label.ql-active .ql-stroke-miter,
  .ql-bubble .ql-toolbar .ql-picker-label.ql-active .ql-stroke-miter,
  .ql-bubble.ql-toolbar .ql-picker-item:hover .ql-stroke-miter,
  .ql-bubble .ql-toolbar .ql-picker-item:hover .ql-stroke-miter,
  .ql-bubble.ql-toolbar .ql-picker-item.ql-selected .ql-stroke-miter,
  .ql-bubble .ql-toolbar .ql-picker-item.ql-selected .ql-stroke-miter {
    stroke: #2196f3;
  }

  .ql-bubble.ql-toolbar button:hover,
  .ql-bubble .ql-toolbar button:hover,
  .ql-bubble.ql-toolbar button:focus,
  .ql-bubble .ql-toolbar button:focus,
  .ql-bubble.ql-toolbar button.ql-active,
  .ql-bubble .ql-toolbar button.ql-active,
  .ql-bubble.ql-toolbar .ql-picker-label:hover,
  .ql-bubble .ql-toolbar .ql-picker-label:hover,
  .ql-bubble.ql-toolbar .ql-picker-label.ql-active,
  .ql-bubble .ql-toolbar .ql-picker-label.ql-active,
  .ql-bubble.ql-toolbar .ql-picker-item:hover,
  .ql-bubble .ql-toolbar .ql-picker-item:hover,
  .ql-bubble.ql-toolbar .ql-picker-item.ql-selected,
  .ql-bubble .ql-toolbar .ql-picker-item.ql-selected {
    color: #2196f3;
  }

  .ql-toolbar.ql-bubble .ql-picker.ql-expanded .ql-picker-options {
    border-color: #ccc;
  }
  .ql-bubble .ql-picker.ql-expanded .ql-picker-options {
    top: 100%;
    z-index: 1;
    display: block;
    margin-top: -1px;
    border: 1px solid #cccccc;
  }
  .ql-toolbar.ql-bubble .ql-picker-options {
    border: 1px solid transparent;
    box-shadow: rgba(0, 0, 0, 0.2) 0 2px 8px;
  }
  .ql-bubble .ql-picker-options {
    position: absolute;
    display: none;
    min-width: 100%;
    height: 200px;
    padding: 4px 2px;
    overflow-y: auto;
    white-space: nowrap;
    background-color: #fff;
  }

  .ql-bubble .ql-tooltip:not(.ql-flip) .ql-tooltip-arrow {
    top: -6px;
    border-bottom: 6px solid #fff;
  }

  .ql-formats {
    margin: 3px 6px 3px 0px;
  }

  .ql-bubble .ql-toolbar .ql-formats:first-child {
    margin-left: 1px;
  }

  .ql-bubble.ql-toolbar button:hover .ql-fill,
  .ql-bubble .ql-toolbar button:hover .ql-fill,
  .ql-bubble.ql-toolbar button:focus .ql-fill,
  .ql-bubble .ql-toolbar button:focus .ql-fill,
  .ql-bubble.ql-toolbar button.ql-active .ql-fill,
  .ql-bubble .ql-toolbar button.ql-active .ql-fill,
  .ql-bubble.ql-toolbar .ql-picker-label:hover .ql-fill,
  .ql-bubble .ql-toolbar .ql-picker-label:hover .ql-fill,
  .ql-bubble.ql-toolbar .ql-picker-label.ql-active .ql-fill,
  .ql-bubble .ql-toolbar .ql-picker-label.ql-active .ql-fill,
  .ql-bubble.ql-toolbar .ql-picker-item:hover .ql-fill,
  .ql-bubble .ql-toolbar .ql-picker-item:hover .ql-fill,
  .ql-bubble.ql-toolbar .ql-picker-item.ql-selected .ql-fill,
  .ql-bubble .ql-toolbar .ql-picker-item.ql-selected .ql-fill,
  .ql-bubble.ql-toolbar button:hover .ql-stroke.ql-fill,
  .ql-bubble .ql-toolbar button:hover .ql-stroke.ql-fill,
  .ql-bubble.ql-toolbar button:focus .ql-stroke.ql-fill,
  .ql-bubble .ql-toolbar button:focus .ql-stroke.ql-fill,
  .ql-bubble.ql-toolbar button.ql-active .ql-stroke.ql-fill,
  .ql-bubble .ql-toolbar button.ql-active .ql-stroke.ql-fill,
  .ql-bubble.ql-toolbar .ql-picker-label:hover .ql-stroke.ql-fill,
  .ql-bubble .ql-toolbar .ql-picker-label:hover .ql-stroke.ql-fill,
  .ql-bubble.ql-toolbar .ql-picker-label.ql-active .ql-stroke.ql-fill,
  .ql-bubble .ql-toolbar .ql-picker-label.ql-active .ql-stroke.ql-fill,
  .ql-bubble.ql-toolbar .ql-picker-item:hover .ql-stroke.ql-fill,
  .ql-bubble .ql-toolbar .ql-picker-item:hover .ql-stroke.ql-fill,
  .ql-bubble.ql-toolbar .ql-picker-item.ql-selected .ql-stroke.ql-fill,
  .ql-bubble .ql-toolbar .ql-picker-item.ql-selected .ql-stroke.ql-fill {
    fill: #2196f3;
  }
  .ql-bubble .ql-picker.ql-size {
    width: 60px;
  }
  .ql-bubble .ql-picker.ql-size .ql-picker-item[data-value='9px']::before {
    font-size: 9px !important;
    content: '9';
  }
  .ql-bubble .ql-picker.ql-size .ql-picker-item[data-value='10px']::before {
    font-size: 10px !important;
    content: '10';
  }
  .ql-bubble .ql-picker.ql-size .ql-picker-item[data-value='11px']::before {
    font-size: 11px !important;
    content: '11';
  }
  .ql-bubble .ql-picker.ql-size .ql-picker-item[data-value='12px']::before {
    font-size: 12px !important;
    content: '12';
  }
  .ql-bubble .ql-picker.ql-size .ql-picker-item[data-value='14px']::before {
    font-size: 14px !important;
    content: '14';
  }
  .ql-bubble .ql-picker.ql-size .ql-picker-item[data-value='16px']::before {
    font-size: 16px !important;
    content: '16';
  }
  .ql-bubble .ql-picker.ql-size .ql-picker-item[data-value='18px']::before {
    font-size: 18px !important;
    content: '18';
  }
  .ql-bubble .ql-picker.ql-size .ql-picker-item[data-value='20px']::before {
    font-size: 20px !important;
    content: '20';
  }
  .ql-bubble .ql-picker.ql-size .ql-picker-item[data-value='22px']::before {
    font-size: 22px !important;
    content: '22';
  }
  .ql-bubble .ql-picker.ql-size .ql-picker-item[data-value='24px']::before {
    font-size: 24px !important;
    content: '24';
  }
  .ql-bubble .ql-picker.ql-size .ql-picker-item[data-value='26px']::before {
    font-size: 26px !important;
    content: '26';
  }
  .ql-bubble .ql-picker.ql-size .ql-picker-item[data-value='36px']::before {
    font-size: 36px !important;
    content: '36';
  }
  .ql-bubble .ql-picker.ql-size .ql-picker-label::before,
  .ql-bubble .ql-picker.ql-size .ql-picker-item::before {
    font-size: 14px !important;
    content: '14px';
  }

  .myclass {
    border: 1px solid transparent;
    cursor: pointer;
    :hover {
      border: 1px solid #58a6e7;
    }
  }
`])),function(a){return a.size==="middle"?"6px 11px 4px 11px":a.size==="small"?"2px 2px 0px 5px":"14px 11px"},function(a){return a.hasError?"1px solid #ff4d4f":"1px solid transparent"},function(a){return a.hasError?"1px solid #ff4d4f !important":"1px solid #1890ff !important"},function(a){return a.innerStyle&&a.innerStyle.fontSize},function(a){return a.innerStyle&&a.innerStyle.textAlign});function Ie(a){return typeof a=="symbol"?"":a&&a.startsWith("<p")?a:"<p>".concat(a,"</p>")}var Le=m.Quill.import("ui/icons");Le.tags='<svg style="width:18px;height:18px;"  viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="512" height="512"><path d="M851.968 167.936l0 109.568-281.6 0 0 587.776-116.736 0 0-587.776-281.6 0 0-109.568 679.936 0z" p-id="3840"></path></svg>';var $=function(n){var t=n.onChange,e=n.value,r=e===void 0?"":e,l=n.placeholder,i=n.style,o=n.className,s=n.bounds,c=(0,f.useState)(!1),b=(0,A.Z)(c,2),C=b[0],M=b[1],Pe=(0,f.useState)(!1),Y=(0,A.Z)(Pe,2),Qe=Y[0],I=Y[1],j=(0,f.useRef)(),G=(0,f.useMemo)(function(){return"quill-".concat((0,De.kb)())},[]),J=function(p){if(p.startsWith("<p>")){var B=p.replace(/^<p>/,"").replace(/<\/p>$/,"");t(B==="<br>"?"":B)}else t(p)};function He(){I(!0)}return(0,f.useEffect)(function(){var v;setTimeout(function(){return M(!1)},50),(v=j.current)===null||v===void 0||v.editor.getModule("toolbar").container.addEventListener("mousedown",function(p){p.preventDefault()})},[]),(0,d.jsxs)(Me,{style:i,className:le()(o,{"is-focus":C,"not-focus":!C}),innerStyle:n.innerStyle,size:n.size,children:[Qe&&(0,d.jsx)(W,{onClose:function(){return I(!1)},value:r,onChange:function(p){p&&J(p),I(!1)}}),(0,d.jsxs)("div",{id:G,className:"ql-formats",children:[(0,d.jsx)(g.Z,{title:"\u5B57\u53F7",children:(0,d.jsxs)("select",{className:"ql-size",children:[(0,d.jsx)("option",{value:"12px",children:"12px"}),(0,d.jsx)("option",{value:"14px",children:"14px"}),(0,d.jsx)("option",{value:"16px",children:"16px"}),(0,d.jsx)("option",{value:"18px",children:"18px"}),(0,d.jsx)("option",{value:"20px",children:"20px"}),(0,d.jsx)("option",{value:"22px",children:"22px"}),(0,d.jsx)("option",{value:"24px",children:"24px"}),(0,d.jsx)("option",{value:"36px",children:"36px"})]})}),(0,d.jsx)(g.Z,{title:"\u5B57\u4F53\u989C\u8272",children:(0,d.jsx)("select",{className:"ql-color"})}),(0,d.jsx)(g.Z,{title:"\u80CC\u666F\u8272",children:(0,d.jsx)("select",{className:"ql-background"})}),(0,d.jsx)(g.Z,{title:"\u52A0\u7C97",children:(0,d.jsx)("button",{className:"ql-bold"})}),(0,d.jsx)(g.Z,{title:"\u659C\u4F53",children:(0,d.jsx)("button",{className:"ql-italic"})}),(0,d.jsx)(g.Z,{title:"\u6E05\u9664\u6837\u5F0F",children:(0,d.jsx)("button",{className:"ql-clean"})}),(0,d.jsx)(g.Z,{title:"\u9AD8\u7EA7\u7F16\u8F91",children:(0,d.jsx)("button",{className:"ql-tags"})})]}),(0,d.jsx)(ne(),{theme:"bubble",value:Ie(r),ref:j,onChange:J,onChangeSelection:function(p,B,K){n.onChangeSelection&&p&&p.index&&n.onChangeSelection(p.index,K.getText())},style:{textAlign:"center"},bounds:s,onFocus:function(){j.current.editor.theme.tooltip.show(),M(!0)},scrollingContainer:"body",className:o,onBlur:function(p,B,K){M(!1),j.current.editor.theme.tooltip.hide()},placeholder:l,formats:["size","color","width","background","header","bold","italic","underline","strike","blockquote","list","bullet","indent","link","image","video","formula","align","imageBlot","code-block"],modules:(0,f.useMemo)(function(){return{toolbar:{container:"#".concat(G),handlers:{tags:He}},clipboard:{matchVisual:!1},syntax:{highlight:function(p){return L.Z.highlightAuto(p).value}}}},[])})]})},Oe=$}}]);
