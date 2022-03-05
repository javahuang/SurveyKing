(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[1389],{17758:function(K,I,s){"use strict";s.d(I,{cM:function(){return ee},Bn:function(){return ie},ZP:function(){return de}});var w=s(2824),P=s(20310),r=s(67294),N=s(71167),S=s.n(N),z=s(29163),R=s(94184),V=s.n(R),O=s(11849),_=s(71194),T=s(50146),M=s(49111),J=s(19650),A=s(57663),L=s(71577),j=s(93224),$=s(76095),x=s.n($),B=s(85551),X=s.n(B),E=s(69610),Z=s(54941),F=s(63543),b=s(94663),n=s(43028),e=s(5869),t=x().import("blots/block"),i=function(f){(0,n.Z)(o,f);var h=(0,e.Z)(o);function o(){return(0,E.Z)(this,o),h.apply(this,arguments)}return(0,Z.Z)(o,[{key:"deleteAt",value:function(p,d){(0,F.Z)((0,b.Z)(o.prototype),"deleteAt",this).call(this,p,d),this.cache={}}}],[{key:"create",value:function(p){var d=(0,F.Z)((0,b.Z)(o),"create",this).call(this,p);if(p===!0)return d;var q=document.createElement("img");return q.setAttribute("src",p),d.appendChild(q),d}},{key:"value",value:function(p){var d=p.dataset,q=d.src,v=d.custom;return{src:q,custom:v}}}]),o}(t);i.blotName="imageBlot",i.className="image-uploading",i.tagName="span",x().register({"formats/imageBlot":i});var a=i,l=function(){function f(h,o){(0,E.Z)(this,f),this.quill=h,this.options=o,this.range=null,typeof this.options.upload!="function"&&console.warn("[Missing config] upload function that returns a promise is required");var c=this.quill.getModule("toolbar");c.addHandler("image",this.selectLocalImage.bind(this)),this.handleDrop=this.handleDrop.bind(this),this.handlePaste=this.handlePaste.bind(this),this.quill.root.addEventListener("drop",this.handleDrop,!1),this.quill.root.addEventListener("paste",this.handlePaste,!1)}return(0,Z.Z)(f,[{key:"selectLocalImage",value:function(){var o=this;this.range=this.quill.getSelection(),this.fileHolder=document.createElement("input"),this.fileHolder.setAttribute("type","file"),this.fileHolder.setAttribute("accept","image/*"),this.fileHolder.setAttribute("style","visibility:hidden"),this.fileHolder.onchange=this.fileChanged.bind(this),document.body.appendChild(this.fileHolder),this.fileHolder.click(),window.requestAnimationFrame(function(){document.body.removeChild(o.fileHolder)})}},{key:"handleDrop",value:function(o){var c=this;if(o.stopPropagation(),o.preventDefault(),o.dataTransfer&&o.dataTransfer.files&&o.dataTransfer.files.length){if(document.caretRangeFromPoint){var p=document.getSelection(),d=document.caretRangeFromPoint(o.clientX,o.clientY);p&&d&&p.setBaseAndExtent(d.startContainer,d.startOffset,d.startContainer,d.startOffset)}else{var q=document.getSelection(),v=document.caretPositionFromPoint(o.clientX,o.clientY);q&&v&&q.setBaseAndExtent(v.offsetNode,v.offset,v.offsetNode,v.offset)}this.range=this.quill.getSelection();var g=o.dataTransfer.files[0];setTimeout(function(){c.range=c.quill.getSelection(),c.readAndUploadFile(g)},0)}}},{key:"handlePaste",value:function(o){var c=this,p=o.clipboardData||window.clipboardData;if(p&&(p.items||p.files))for(var d=p.items||p.files,q=/^image\/(jpe?g|gif|png|svg|webp)$/i,v=0;v<d.length;v++)q.test(d[v].type)&&function(){var g=d[v].getAsFile?d[v].getAsFile():d[v];g&&(c.range=c.quill.getSelection(),o.preventDefault(),setTimeout(function(){c.range=c.quill.getSelection(),c.readAndUploadFile(g)},0))}()}},{key:"readAndUploadFile",value:function(o){var c=this,p=!1,d=new FileReader;d.addEventListener("load",function(){if(!p){var q=d.result;c.insertBase64Image(q)}},!1),o&&d.readAsDataURL(o),this.options.upload(o).then(function(q){c.insertToEditor(q)},function(q){p=!0,c.removeBase64Image(),console.warn(q)})}},{key:"fileChanged",value:function(){var o=this.fileHolder.files[0];this.readAndUploadFile(o)}},{key:"insertBase64Image",value:function(o){var c=this.range;this.quill.insertEmbed(c.index,a.blotName,"".concat(o),"user")}},{key:"insertToEditor",value:function(o){var c=this.range;this.quill.deleteText(c.index,3,"user"),console.log(c,o),this.quill.insertEmbed(c.index,"image","".concat(o)),c.index++,this.quill.setSelection(c,"user")}},{key:"removeBase64Image",value:function(){var o=this.range;this.quill.deleteText(o.index,3,"user")}}]),f}();window.ImageUploader=l;var u=l,y=function(){function f(h,o){(0,E.Z)(this,f),this.quill=h,this.options=o,this.container=document.querySelector(o.container),h.on("text-change",this.update.bind(this)),this.update()}return(0,Z.Z)(f,[{key:"calculate",value:function(){var o=this.quill.getText();return this.options.unit==="word"?(o=o.trim(),o.length>0?o.split(/\s+/).length:0):o.length}},{key:"update",value:function(){var o=this.calculate(),c=this.options.unit;o!==1&&(c+="s")}}]),f}();N.Quill.register("modules/counter",y);var k=s(68489),m=s(85893);x().register("modules/imageResize",X()),x().register("modules/imageUploader",u);var U=x().import("attributors/style/size");U.whitelist=["9px","10px","11px","12px","14px","16px","18px","20px","22px","24px","26px","36px"],x().register(U,!0);var ae=function(h){var o=(0,r.useRef)(null),c=(0,r.useRef)(),p=(0,r.useRef)();return(0,r.useEffect)(function(){return o.current&&(c.current=new(x())(o.current,{theme:"snow",formats:["size","color","background","header","bold","italic","underline","strike","blockquote","list","bullet","indent","link","image","formula","align","imageBlot"],modules:{counter:{container:"#counter",unit:"word"},imageResize:{parchment:x().import("parchment"),modules:["Resize","DisplaySize"]},imageUploader:{upload:function(q){return new Promise(function(v,g){k.hi.uploadImage(3,q).then(function(C){C.success&&v("/api/public/preview/".concat(C.data.id))}).catch(function(C){g("Upload failed"),console.error("Error:",C)})})}},toolbar:[[{size:["9px","10px","11px","12px","14px","16px","18px","20px","22px","24px","26px","36px"]}],[{color:[]},{background:[]}],["bold","italic","underline","strike","blockquote"],[{align:""},{align:"center"},{align:"right"},{align:"justify"}],["link","image"],[{list:"ordered"},{list:"bullet"},{indent:"-1"},{indent:"+1"}]]}}),h.value&&c.current.setText(h.value),c.current.on("text-change",function(d,q,v){var g,C=(g=c.current)===null||g===void 0?void 0:g.root.innerHTML;C!==p.current&&h.onChange&&(h.onChange(C||""),p.current=C)})),function(){}},[]),(0,r.useEffect)(function(){var d,q=p.current,v=(d=h.value)!==null&&d!==void 0?d:"";if(v!==q){var g;p.current=v,(g=c.current)===null||g===void 0||g.setContents(c.current.clipboard.convert(v))}},[h.value]),(0,m.jsx)("div",{className:"custom-quill-editor",children:(0,m.jsx)("div",{ref:o})})},ee=function(h){var o=h.onChange,c=h.onClose,p=h.value,d=(0,j.Z)(h,["onChange","onClose","value"]),q=(0,r.useState)(p),v=(0,w.Z)(q,2),g=v[0],C=v[1];return(0,m.jsx)(T.Z,(0,O.Z)((0,O.Z)({visible:!0,onCancel:c,footer:!1,maskClosable:!1,closable:!1,width:650},d),{},{children:(0,m.jsxs)("div",{children:[(0,m.jsx)(ae,{value:g,onChange:function(H){C(H)}}),(0,m.jsx)("div",{style:{textAlign:"right",marginTop:20},children:(0,m.jsxs)(J.Z,{children:[(0,m.jsx)(L.Z,{onClick:c,children:"\u53D6\u6D88"}),(0,m.jsx)(L.Z,{type:"primary",onClick:function(){return o(g)},children:"\u4FDD\u5B58"})]})})]})}))},se=s(8166),te;window.katex=se.Z;var ne=N.Quill.import("attributors/style/size");ne.whitelist=["9px","10px","11px","12px","14px","16px","18px","20px","22px","24px","26px","36px"],N.Quill.register(ne,!0);var ve=N.Quill.import("modules/clipboard"),qe=N.Quill.import("delta"),me=null,ue=z.ZP.div(te||(te=(0,P.Z)([`
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

  .ql-bubble .ql-tooltip {
    z-index: 999;
    background-color: #fff;
    border: 1px solid #eaeaea;
    border-radius: 2px;
    box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.1);
    cursor: pointer;
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

  .ql-bubble .ql-toolbar .ql-formats {
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
`])),function(f){return f.size==="middle"?"6px 11px 4px 11px":f.size==="small"?"2px 2px 0px 5px":"14px 11px"},function(f){return f.hasError?"1px solid #ff4d4f":"1px solid transparent"},function(f){return f.hasError?"1px solid #ff4d4f !important":"1px solid #1890ff !important"},function(f){return f.innerStyle&&f.innerStyle.fontSize},function(f){return f.innerStyle&&f.innerStyle.textAlign});function ce(f){return typeof f=="symbol"?"":f&&f.startsWith("<p")?f:"<p>".concat(f,"</p>")}var he=N.Quill.import("ui/icons");he.tags='<svg style="width:18px;height:18px;"  viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="512" height="512"><path d="M851.968 167.936l0 109.568-281.6 0 0 587.776-116.736 0 0-587.776-281.6 0 0-109.568 679.936 0z" p-id="3840"></path></svg>';var ie=function(h){var o=h.onChange,c=h.value,p=c===void 0?"":c,d=h.placeholder,q=h.style,v=h.className,g=h.bounds,C=(0,r.useState)(!1),W=(0,w.Z)(C,2),H=W[0],Y=W[1],be=(0,r.useState)(!1),le=(0,w.Z)(be,2),fe=le[0],G=le[1],re=function(D){if(D.startsWith("<p>")){var oe=D.replace(/^<p>/,"").replace(/<\/p>$/,"");o(oe==="<br>"?"":oe)}else o(D)};function pe(){G(!0)}return(0,r.useEffect)(function(){setTimeout(function(){return Y(!1)},50)},[]),(0,m.jsxs)(ue,{style:q,className:V()(v,{"is-focus":H,"not-focus":!H}),innerStyle:h.innerStyle,size:h.size,children:[fe&&(0,m.jsx)(ee,{onClose:function(){return G(!1)},value:p,onChange:function(D){D&&re(D),G(!1)}}),(0,m.jsx)(S(),{theme:"bubble",value:ce(p),onChange:re,style:{textAlign:"center"},bounds:g,onFocus:function(){Y(!0)},scrollingContainer:"body",className:v,onBlur:function(){Y(!1)},placeholder:d,modules:(0,r.useMemo)(function(){return{toolbar:{container:[[{size:["9px","10px","11px","12px","14px","16px","18px","20px","22px","24px","26px","36px"]}],[{color:[]},{background:[]}],["bold","italic"],["clean","tags"]],handlers:{tags:pe}},clipboard:{matchVisual:!1}}},[])})]})},de=ie},71211:function(K,I,s){"use strict";s.d(I,{j$:function(){return F},Ox:function(){return A}});var w=s(69610),P=s(54941),r=s(54531),N=function(){function b(n,e){(0,w.Z)(this,b),this.context=void 0,this.props=void 0,this.current=0,this.history=[],this.updateTimer=null,this.maxSize=100,this.context=n,this.props=e,this.push(),this.makeObservable()}return(0,P.Z)(b,[{key:"makeObservable",value:function(){(0,r.Ou)(this,{allowRedo:r.LO.computed,allowUndo:r.LO.computed,current:r.LO.ref,history:r.LO.shallow,push:r.aD,undo:r.aD,redo:r.aD,goTo:r.aD,clear:r.aD})}},{key:"list",value:function(){return this.history}},{key:"push",value:function(){this.current<this.history.length-1&&(this.history=this.history.slice(0,this.current+1)),this.current=this.history.length,this.history.push(this.context.serialize());var e=this.history.length-this.maxSize;e>0&&(this.history.splice(0,e),this.current=this.history.length-1)}},{key:"allowUndo",get:function(){return this.history.length>0&&this.current-1>=0}},{key:"allowRedo",get:function(){return this.history.length>this.current+1}},{key:"redo",value:function(){if(this.allowRedo){var e,t=this.history[this.current+1];this.context.from(t),this.current++,(e=this.props)!==null&&e!==void 0&&e.onRedo&&this.props.onRedo(t)}}},{key:"undo",value:function(){if(this.allowUndo){var e,t=this.history[this.current-1];this.context.from(t),this.current--,(e=this.props)!==null&&e!==void 0&&e.onUndo&&this.props.onUndo(t)}}},{key:"goTo",value:function(e){this.history[e]&&(this.context.from(this.history[e]),this.current=e)}},{key:"clear",value:function(){this.history=[],this.current=0}}]),b}(),S=s(76826),z=s(86582),R=s(11849),V=s(93224),O=s(64031),_=s(77596),T=new Map,M=new Set,J=(0,_.kP)("1234567890abcdefghijklmnopqrstuvwxyz",4);function A(){var b=J();return M.has(b)?A():(M.add(b),b)}function L(b){for(var n=arguments.length,e=new Array(n>1?n-1:0),t=1;t<n;t++)e[t-1]=arguments[t];return JSON.parse(JSON.stringify(b,function(i,a){if(a!=null&&!(a instanceof Array&&a.length===0)&&e.indexOf(i)===-1&&!i.startsWith("_"))return a}))}var j=function(n,e){var t=function l(u){u.depth=u.parent?u.parent.depth+1:0,u.children.forEach(l)},i=function(u){u.parent=e,u.root=e.root,t(u)},a=function(u){return i(u),j(u.children,u),u};return n.map(function(l){return a(l),T.has(l.id)||T.set(l.id,l),l})},$=function(n){n.parent&&(n.parent.children=n.parent.children.filter(function(e){return e!==n}))},x=function(){function b(n,e){var t=this;(0,w.Z)(this,b),this.parent=void 0,this.root=void 0,this.operation=void 0,this.form=void 0,this.id=void 0,this.title=void 0,this.depth=0,this.index=0,this.hidden=!1,this.focus=!1,this.type=void 0,this.children=[];var i=n.schema,a=i.children,l=(0,V.Z)(i,["children"]);this.id=n.schema.id||A(),this.title=n.schema.title||"",l.dataSource&&l.dataSource.forEach(function(u){u.value||(u.value=A())}),l.row&&l.row.forEach(function(u){u.id||(u.id=A())}),this.form=(0,O.Np)({initialValues:l,effects:function(y){(0,O.Zj)("title",function(k){t.title=k.value}),(0,O.Zj)("type",function(k){t.type=k.value})}}),this.children=a&&a.map(function(u){return b.fromSchema(u,t)})||[],this.type=l.type,e?(this.parent=e,this.depth=e.depth+1,this.root=e.root):this.root=this,T.set(this.id,this),this.operation=n==null?void 0:n.operation,this.makeObservable(),this.resetIndex()}return(0,P.Z)(b,[{key:"isVoid",get:function(){return this.type==="Pagination"||this.type==="SplitLine"||this.type==="Remark"}},{key:"actionVisible",get:function(){return!(this.type==="Pagination"&&this.index===0)}},{key:"makeObservable",value:function(){(0,r.Ou)(this,{type:r.LO.ref,parent:r.LO.ref,actionVisible:r.LO.computed,isVoid:r.LO.computed,hidden:r.LO.ref,focus:r.LO,form:r.LO.ref,index:r.LO,children:r.LO.shallow,prependNode:r.aD,appendNodeAtIndex:r.aD,remove:r.aD})}},{key:"serialize",value:function(){for(var e=arguments.length,t=new Array(e),i=0;i<e;i++)t[i]=arguments[i];return L.apply(void 0,[(0,R.Z)((0,R.Z)({},(0,r.ZN)(this.form.values)),{},{id:this.id,type:this.type,children:this.children.map(function(a){return a.serialize()})})].concat(t))}},{key:"findById",value:function(e){var t;if(!!e){if(this.id===e)return this;if(((t=this.children)===null||t===void 0?void 0:t.length)>0)return T.get(e)}}},{key:"getParents",value:function(e){var t=e||this;return t!=null&&t.parent?[t.parent].concat(this.getParents(t.parent)):[]}},{key:"getParentByDepth",value:function(){var e=arguments.length>0&&arguments[0]!==void 0?arguments[0]:0,t=this.parent;return(t==null?void 0:t.depth)===e?t:t==null?void 0:t.getParentByDepth(e)}},{key:"contains",value:function(){for(var e=this,t=arguments.length,i=new Array(t),a=0;a<t;a++)i[a]=arguments[a];return i.every(function(l){return l===e||(l==null?void 0:l.parent)===e||(l==null?void 0:l.getParentByDepth(e.depth))===e})}},{key:"triggerMutation",value:function(){var e;this!==null&&this!==void 0&&(e=this.root)!==null&&e!==void 0&&e.operation&&this.root.operation.snapshot()}},{key:"resetNodesParent",value:function(e,t){var i=this;return j(e.filter(function(a){return a!==i}),t)}},{key:"resetIndex",value:function(){if(this.children){var e=0,t=0;this.children.forEach(function(i,a){i.isVoid?i.type==="Pagination"?(i.index=t,t++):i.index=-1:(i.index=e,e++)})}}},{key:"prependNode",value:function(){for(var e=this,t=arguments.length,i=new Array(t),a=0;a<t;a++)i[a]=arguments[a];if(i.some(function(u){return u.contains(e)}))return[];var l=this.resetNodesParent(i,this);if(!l.length)return[];this.children=l.concat(this.children),this.triggerMutation(),this.resetIndex()}},{key:"appendNode",value:function(){for(var e=this,t=arguments.length,i=new Array(t),a=0;a<t;a++)i[a]=arguments[a];if(i.some(function(u){return u.contains(e)}))return[];var l=this.resetNodesParent(i,this);if(!l.length)return[];this.children=S.VS.apply(void 0,[this.children,this.children.length].concat((0,z.Z)(l))),this.resetIndex()}},{key:"appendNodeAtIndex",value:function(e){for(var t=this,i=arguments.length,a=new Array(i>1?i-1:0),l=1;l<i;l++)a[l-1]=arguments[l];if(a.some(function(y){return y.contains(t)}))return[];var u=this.resetNodesParent(a,this);if(!u.length)return[];this.children=S.VS.apply(void 0,[this.children,e].concat(a)),this.resetIndex()}},{key:"insertAfter",value:function(){for(var e=this,t,i=this.parent,a=arguments.length,l=new Array(a),u=0;u<a;u++)l[u]=arguments[u];if(l.some(function(k){return k.contains(e)}))return[];if(i!=null&&(t=i.children)!==null&&t!==void 0&&t.length){var y=this.resetNodesParent(l,i);if(!y.length)return[];i.children=i.children.reduce(function(k,m){return m===e?k.concat([m]).concat(y):k.concat([m])},[])}return[]}},{key:"deleteNode",value:function(){for(var e=arguments.length,t=new Array(e),i=0;i<e;i++)t[i]=arguments[i];this.children=this.children.filter(function(a){return!t.includes(a)}),T.delete(this.id)}},{key:"remove",value:function(){var e;$(this),(e=this.parent)===null||e===void 0||e.resetIndex()}},{key:"copy",value:function(){var e,t=b.fromSchema(this.serialize("id"));(e=this.parent)===null||e===void 0||e.appendNodeAtIndex(this.index+1,t)}}],[{key:"fromSchema",value:function(e,t){return new b({schema:e},t)}}]),b}(),B=(0,S.JH)(),X=function(){function b(n){(0,w.Z)(this,b),this.workspace=void 0,this.requests={snapshot:-1},this.focusNode=void 0,this.workspace=n,this.makeObservable()}return(0,P.Z)(b,[{key:"makeObservable",value:function(){(0,r.Ou)(this,{focusNode:r.LO.ref,removeNodes:r.aD,cloneNodes:r.aD,dropAddNode:r.aD,dropSortNode:r.aD,addNode:r.aD,addNodeWithTemplate:r.aD,switchFocusNode:r.aD,focusClean:r.aD,batchAddNode:r.aD,batchReplaceNode:r.aD,changePreviewTheme:r.aD,changePreviewType:r.aD,changeWorkbench:r.aD,changeBackgroundImage:r.aD,templatePreview:r.aD,fromTemplate:r.aD})}},{key:"snapshot",value:function(){var e=this;(0,S.mO)(this.requests.snapshot),this.requests.snapshot=(0,S.LQ)(function(){e.workspace.history.push()})}},{key:"dropAddNode",value:function(e,t){var i=t.type,a=t.addedIndex,l=t.template;if(i&&!l&&(l=B.getQuestionTemplate(i)),!!l){var u=x.fromSchema(l);e.appendNodeAtIndex(a,u);var y=this.workspace.tree;if(l.type==="Pagination"&&y.children.filter(function(m){return m.type==="Pagination"}).length===1){var k=x.fromSchema(l);y.prependNode(k)}this.snapshot()}}},{key:"dropSortNode",value:function(e,t,i){e.remove(),t.appendNodeAtIndex(i,e),this.snapshot()}},{key:"addNode",value:function(e,t){var i=B.getQuestionTemplate(e);return this.addNodeWithTemplate(i,t)}},{key:"addNodeWithTemplate",value:function(e,t){var i=this,a=this.workspace.tree,l=x.fromSchema(e,t);if(t)t.appendNode(l);else if(e.type==="QuestionSet"||e.type==="Pagination"){if(a.appendNode(l),e.type==="Pagination"&&a.children.filter(function(k){return k.type==="Pagination"}).length===1){var u=x.fromSchema(e,t);a.prependNode(u)}e.type==="Pagination"&&this.rebuildPagination()}else{var y=a.children.slice(-1)[0];y&&y.type==="QuestionSet"?y.appendNode(l):a.appendNode(l),setTimeout(function(){i.switchFocusNode(l)},20)}return this.snapshot(),l}},{key:"batchAddNode",value:function(e,t){e.forEach(function(i){t.appendNode(x.fromSchema(i,t))}),this.snapshot()}},{key:"batchReplaceNode",value:function(e,t){t.children=[],this.batchAddNode(e,t),this.snapshot()}},{key:"removeNodes",value:function(){for(var e=this,t=arguments.length,i=new Array(t),a=0;a<t;a++)i[a]=arguments[a];i.forEach(function(l){if(l.type==="Pagination"){var u,y=(u=l.parent)===null||u===void 0?void 0:u.children.filter(function(U){return U.type==="Pagination"}).length;if(y===2){var k,m=(k=l.parent)===null||k===void 0?void 0:k.children[0];m&&m.type==="Pagination"&&m.remove()}e.rebuildPagination()}l.remove()}),this.snapshot()}},{key:"copyNode",value:function(e){e.copy(),this.snapshot()}},{key:"cloneNodes",value:function(){}},{key:"switchFocusNode",value:function(e){this.focusNode!==e&&(this.focusNode&&(this.focusNode.focus=!1),this.focusNode=e,e.focus=!0)}},{key:"focusClean",value:function(){this.focusNode&&(this.focusNode.focus=!1),this.focusNode=void 0}},{key:"changePreviewTheme",value:function(e){this.workspace.workbench.type==="PREVIEW"&&(this.workspace.workbench.previewTheme=e)}},{key:"changePreviewType",value:function(e){this.workspace.workbench.type==="PREVIEW"&&(this.workspace.workbench.previewType=e)}},{key:"changeWorkbench",value:function(e){this.workspace.workbench.type=e}},{key:"rebuildPagination",value:function(){var e=this.workspace.tree.children.filter(function(t){return t.type==="Pagination"});e.forEach(function(t,i){t.form.values.attribute={currentPage:i+1,totalPage:e.length}})}},{key:"changeBackgroundImage",value:function(e,t){console.log(e,t);var i=this.workspace.tree;i.form.values.attribute||(i.form.values.attribute={}),i.form.values.attribute[e]=t,this.snapshot()}},{key:"templatePreview",value:function(e){this.snapshot(),this.workspace.workbench.previewType="PC",this.workspace.workbench.type="PREVIEW",this.workspace.from(e)}},{key:"fromTemplate",value:function(e){this.snapshot(),this.workspace.from(e),this.workspace.workbench.type="EDITOR"}}]),b}(),E=function(){function b(n){(0,w.Z)(this,b),this.type=void 0,this.previewTheme=void 0,this.previewType=void 0,this.operation=void 0,this.type="EDITOR",this.previewTheme="antd",this.previewType="PHONE",this.operation=n,this.makeObservable()}return(0,P.Z)(b,[{key:"makeObservable",value:function(){(0,r.Ou)(this,{type:r.LO.ref,previewType:r.LO.ref,previewTheme:r.LO.ref})}}]),b}(),Z=(0,S.JH)(),F=function(){function b(n){var e=this;(0,w.Z)(this,b),this.history=void 0,this.operation=void 0,this.tree=void 0,this.workbench=void 0,this.operation=new X(this),this.tree=new x({operation:this.operation,schema:n||Z.getQuestionTemplate("Survey")}),this.workbench=new E(this.operation),this.history=new N(this,{onRedo:function(i){e.from(i),e.operation.focusClean()},onUndo:function(i){e.from(i),e.operation.focusClean()}}),this.makeObservable()}return(0,P.Z)(b,[{key:"makeObservable",value:function(){(0,r.Ou)(this,{tree:r.LO.ref})}},{key:"serialize",value:function(){return this.tree.serialize()}},{key:"from",value:function(e){this.tree=x.fromSchema(e)}},{key:"setId",value:function(e){this.tree.id=e}},{key:"getEventContext",value:function(){return{workspace:this}}},{key:"dispatch",value:function(e){}}]),b}()},11628:function(K,I,s){"use strict";s.d(I,{xI:function(){return P},Ge:function(){return S},IE:function(){return N}});var w=s(67294),P=(0,w.createContext)({}),r=P.Provider;function N(){var z=(0,w.useContext)(P);return z.store}function S(){var z=N();return z.flowStore}}}]);
