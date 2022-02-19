(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[1117],{8212:function(Be,X,t){"use strict";t.d(X,{Z:function(){return oe}});var h=t(28991),s=t(67294),a={icon:{tag:"svg",attrs:{viewBox:"64 64 896 896",focusable:"false"},children:[{tag:"path",attrs:{d:"M257.7 752c2 0 4-.2 6-.5L431.9 722c2-.4 3.9-1.3 5.3-2.8l423.9-423.9a9.96 9.96 0 000-14.1L694.9 114.9c-1.9-1.9-4.4-2.9-7.1-2.9s-5.2 1-7.1 2.9L256.8 538.8c-1.5 1.5-2.4 3.3-2.8 5.3l-29.5 168.2a33.5 33.5 0 009.4 29.8c6.6 6.4 14.9 9.9 23.8 9.9zm67.4-174.4L687.8 215l73.3 73.3-362.7 362.6-88.9 15.7 15.6-89zM880 836H144c-17.7 0-32 14.3-32 32v36c0 4.4 3.6 8 8 8h784c4.4 0 8-3.6 8-8v-36c0-17.7-14.3-32-32-32z"}}]},name:"edit",theme:"outlined"},M=a,J=t(27029),H=function(he,G){return s.createElement(J.Z,(0,h.Z)((0,h.Z)({},he),{},{ref:G,icon:M}))};H.displayName="EditOutlined";var oe=s.forwardRef(H)},70347:function(){},17758:function(Be,X,t){"use strict";t.d(X,{cM:function(){return Q},Bn:function(){return ce},ZP:function(){return je}});var h=t(2824),s=t(20310),a=t(67294),M=t(71167),J=t.n(M),H=t(29163),oe=t(94184),W=t.n(oe),he=t(11849),G=t(71194),Ie=t(50146),ke=t(49111),Te=t(19650),me=t(57663),se=t(71577),E=t(93224),Me=t(76095),N=t.n(Me),Re=t(85551),ee=t.n(Re),T=t(69610),n=t(54941),y=t(63543),Z=t(94663),r=t(43028),q=t(5869),L=N().import("blots/block"),e=function(p){(0,r.Z)(l,p);var d=(0,q.Z)(l);function l(){return(0,T.Z)(this,l),d.apply(this,arguments)}return(0,n.Z)(l,[{key:"deleteAt",value:function(x,f){(0,y.Z)((0,Z.Z)(l.prototype),"deleteAt",this).call(this,x,f),this.cache={}}}],[{key:"create",value:function(x){var f=(0,y.Z)((0,Z.Z)(l),"create",this).call(this,x);if(x===!0)return f;var C=document.createElement("img");return C.setAttribute("src",x),f.appendChild(C),f}},{key:"value",value:function(x){var f=x.dataset,C=f.src,c=f.custom;return{src:C,custom:c}}}]),l}(L);e.blotName="imageBlot",e.className="image-uploading",e.tagName="span",N().register({"formats/imageBlot":e});var V=e,te=function(){function p(d,l){(0,T.Z)(this,p),this.quill=d,this.options=l,this.range=null,typeof this.options.upload!="function"&&console.warn("[Missing config] upload function that returns a promise is required");var u=this.quill.getModule("toolbar");u.addHandler("image",this.selectLocalImage.bind(this)),this.handleDrop=this.handleDrop.bind(this),this.handlePaste=this.handlePaste.bind(this),this.quill.root.addEventListener("drop",this.handleDrop,!1),this.quill.root.addEventListener("paste",this.handlePaste,!1)}return(0,n.Z)(p,[{key:"selectLocalImage",value:function(){var l=this;this.range=this.quill.getSelection(),this.fileHolder=document.createElement("input"),this.fileHolder.setAttribute("type","file"),this.fileHolder.setAttribute("accept","image/*"),this.fileHolder.setAttribute("style","visibility:hidden"),this.fileHolder.onchange=this.fileChanged.bind(this),document.body.appendChild(this.fileHolder),this.fileHolder.click(),window.requestAnimationFrame(function(){document.body.removeChild(l.fileHolder)})}},{key:"handleDrop",value:function(l){var u=this;if(l.stopPropagation(),l.preventDefault(),l.dataTransfer&&l.dataTransfer.files&&l.dataTransfer.files.length){if(document.caretRangeFromPoint){var x=document.getSelection(),f=document.caretRangeFromPoint(l.clientX,l.clientY);x&&f&&x.setBaseAndExtent(f.startContainer,f.startOffset,f.startContainer,f.startOffset)}else{var C=document.getSelection(),c=document.caretPositionFromPoint(l.clientX,l.clientY);C&&c&&C.setBaseAndExtent(c.offsetNode,c.offset,c.offsetNode,c.offset)}this.range=this.quill.getSelection();var F=l.dataTransfer.files[0];setTimeout(function(){u.range=u.quill.getSelection(),u.readAndUploadFile(F)},0)}}},{key:"handlePaste",value:function(l){var u=this,x=l.clipboardData||window.clipboardData;if(x&&(x.items||x.files))for(var f=x.items||x.files,C=/^image\/(jpe?g|gif|png|svg|webp)$/i,c=0;c<f.length;c++)C.test(f[c].type)&&function(){var F=f[c].getAsFile?f[c].getAsFile():f[c];F&&(u.range=u.quill.getSelection(),l.preventDefault(),setTimeout(function(){u.range=u.quill.getSelection(),u.readAndUploadFile(F)},0))}()}},{key:"readAndUploadFile",value:function(l){var u=this,x=!1,f=new FileReader;f.addEventListener("load",function(){if(!x){var C=f.result;u.insertBase64Image(C)}},!1),l&&f.readAsDataURL(l),this.options.upload(l).then(function(C){u.insertToEditor(C)},function(C){x=!0,u.removeBase64Image(),console.warn(C)})}},{key:"fileChanged",value:function(){var l=this.fileHolder.files[0];this.readAndUploadFile(l)}},{key:"insertBase64Image",value:function(l){var u=this.range;this.quill.insertEmbed(u.index,V.blotName,"".concat(l),"user")}},{key:"insertToEditor",value:function(l){var u=this.range;this.quill.deleteText(u.index,3,"user"),console.log(u,l),this.quill.insertEmbed(u.index,"image","".concat(l)),u.index++,this.quill.setSelection(u,"user")}},{key:"removeBase64Image",value:function(){var l=this.range;this.quill.deleteText(l.index,3,"user")}}]),p}();window.ImageUploader=te;var ne=te,$=function(){function p(d,l){(0,T.Z)(this,p),this.quill=d,this.options=l,this.container=document.querySelector(l.container),d.on("text-change",this.update.bind(this)),this.update()}return(0,n.Z)(p,[{key:"calculate",value:function(){var l=this.quill.getText();return this.options.unit==="word"?(l=l.trim(),l.length>0?l.split(/\s+/).length:0):l.length}},{key:"update",value:function(){var l=this.calculate(),u=this.options.unit;l!==1&&(u+="s")}}]),p}();M.Quill.register("modules/counter",$);var Ce=t(68489),D=t(85893);N().register("modules/imageResize",ee()),N().register("modules/imageUploader",ne);var le=N().import("attributors/style/size");le.whitelist=["9px","10px","11px","12px","14px","16px","18px","20px","22px","24px","26px","36px"],N().register(le,!0);var w=function(d){var l=(0,a.useRef)(null),u=(0,a.useRef)(),x=(0,a.useRef)();return(0,a.useEffect)(function(){return l.current&&(u.current=new(N())(l.current,{theme:"snow",formats:["size","color","background","header","bold","italic","underline","strike","blockquote","list","bullet","indent","link","image","formula","align","imageBlot"],modules:{counter:{container:"#counter",unit:"word"},imageResize:{parchment:N().import("parchment"),modules:["Resize","DisplaySize"]},imageUploader:{upload:function(C){return new Promise(function(c,F){Ce.hi.uploadImage(3,C).then(function(P){P.success&&c("/api/public/preview/".concat(P.data.id))}).catch(function(P){F("Upload failed"),console.error("Error:",P)})})}},toolbar:[[{size:["9px","10px","11px","12px","14px","16px","18px","20px","22px","24px","26px","36px"]}],[{color:[]},{background:[]}],["bold","italic","underline","strike","blockquote"],[{align:""},{align:"center"},{align:"right"},{align:"justify"}],["link","image"],[{list:"ordered"},{list:"bullet"},{indent:"-1"},{indent:"+1"}]]}}),d.value&&u.current.setText(d.value),u.current.on("text-change",function(f,C,c){var F,P=(F=u.current)===null||F===void 0?void 0:F.root.innerHTML;P!==x.current&&d.onChange&&(d.onChange(P||""),x.current=P)})),function(){}},[]),(0,a.useEffect)(function(){var f,C=x.current,c=(f=d.value)!==null&&f!==void 0?f:"";if(c!==C){var F;x.current=c,(F=u.current)===null||F===void 0||F.setContents(u.current.clipboard.convert(c))}},[d.value]),(0,D.jsx)("div",{className:"custom-quill-editor",children:(0,D.jsx)("div",{ref:l})})},Q=function(d){var l=d.onChange,u=d.onClose,x=d.value,f=(0,E.Z)(d,["onChange","onClose","value"]),C=(0,a.useState)(x),c=(0,h.Z)(C,2),F=c[0],P=c[1];return(0,D.jsx)(Ie.Z,(0,he.Z)((0,he.Z)({visible:!0,onCancel:u,footer:!1,maskClosable:!1,closable:!1,width:650},f),{},{children:(0,D.jsxs)("div",{children:[(0,D.jsx)(w,{value:F,onChange:function(K){P(K)}}),(0,D.jsx)("div",{style:{textAlign:"right",marginTop:20},children:(0,D.jsxs)(Te.Z,{children:[(0,D.jsx)(se.Z,{onClick:u,children:"\u53D6\u6D88"}),(0,D.jsx)(se.Z,{type:"primary",onClick:function(){return l(F)},children:"\u4FDD\u5B58"})]})})]})}))},pe=t(8166),fe;window.katex=pe.Z;var ge=M.Quill.import("attributors/style/size");ge.whitelist=["9px","10px","11px","12px","14px","16px","18px","20px","22px","24px","26px","36px"],M.Quill.register(ge,!0);var Pe=M.Quill.import("modules/clipboard"),Le=M.Quill.import("delta"),Ae=null,ye=H.ZP.div(fe||(fe=(0,s.Z)([`
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
`])),function(p){return p.size==="middle"?"6px 11px 4px 11px":p.size==="small"?"2px 2px 0px 5px":"14px 11px"},function(p){return p.hasError?"1px solid #ff4d4f":"1px solid transparent"},function(p){return p.hasError?"1px solid #ff4d4f !important":"1px solid #1890ff !important"},function(p){return p.innerStyle&&p.innerStyle.fontSize},function(p){return p.innerStyle&&p.innerStyle.textAlign});function Oe(p){return typeof p=="symbol"?"":p&&p.startsWith("<p")?p:"<p>".concat(p,"</p>")}var ue=M.Quill.import("ui/icons");ue.tags='<svg style="width:18px;height:18px;"  viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="512" height="512"><path d="M851.968 167.936l0 109.568-281.6 0 0 587.776-116.736 0 0-587.776-281.6 0 0-109.568 679.936 0z" p-id="3840"></path></svg>';var ce=function(d){var l=d.onChange,u=d.value,x=u===void 0?"":u,f=d.placeholder,C=d.style,c=d.className,F=d.bounds,P=(0,a.useState)(!1),de=(0,h.Z)(P,2),K=de[0],xe=de[1],Se=(0,a.useState)(!1),qe=(0,h.Z)(Se,2),De=qe[0],Ee=qe[1],be=function(ae){if(ae.startsWith("<p>")){var re=ae.replace(/^<p>/,"").replace(/<\/p>$/,"");l(re==="<br>"?"":re)}else l(ae)};function Ze(){Ee(!0)}return(0,a.useEffect)(function(){setTimeout(function(){return xe(!1)},50)},[]),(0,D.jsxs)(ye,{style:C,className:W()(c,{"is-focus":K,"not-focus":!K}),innerStyle:d.innerStyle,size:d.size,children:[De&&(0,D.jsx)(Q,{onClose:function(){return Ee(!1)},value:x,onChange:function(ae){ae&&be(ae),Ee(!1)}}),(0,D.jsx)(J(),{theme:"bubble",value:Oe(x),onChange:be,style:{textAlign:"center"},bounds:F,onFocus:function(){xe(!0)},scrollingContainer:"body",className:c,onBlur:function(){xe(!1)},placeholder:f,modules:(0,a.useMemo)(function(){return{toolbar:{container:[[{size:["9px","10px","11px","12px","14px","16px","18px","20px","22px","24px","26px","36px"]}],[{color:[]},{background:[]}],["bold","italic"],["clean","tags"]],handlers:{tags:Ze}},clipboard:{matchVisual:!1}}},[])})]})},je=ce},26610:function(Be,X,t){"use strict";t.r(X),t.d(X,{Setting:function(){return We},default:function(){return pt}});var h=t(2299),s=t(67294),a=(0,s.createContext)({});function M(){var v=arguments.length>0&&arguments[0]!==void 0?arguments[0]:"",o=(0,s.useContext)(a),i=o.prefixCls;return i+v}var J=t(58024),H=t(39144),oe=t(13062),W=t(71230),he=t(57663),G=t(71577),Ie=t(34792),ke=t(48086),Te=t(48736),me=t(27049),se=t(89032),E=t(15746),Me=t(22385),N=t(31097),Re=t(63185),ee=t(9676),T=t(2824),n=t(28991),y={icon:{tag:"svg",attrs:{viewBox:"64 64 896 896",focusable:"false"},children:[{tag:"path",attrs:{d:"M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 708c-22.1 0-40-17.9-40-40s17.9-40 40-40 40 17.9 40 40-17.9 40-40 40zm62.9-219.5a48.3 48.3 0 00-30.9 44.8V620c0 4.4-3.6 8-8 8h-48c-4.4 0-8-3.6-8-8v-21.5c0-23.1 6.7-45.9 19.9-64.9 12.9-18.6 30.9-32.8 52.1-40.9 34-13.1 56-41.6 56-72.7 0-44.1-43.1-80-96-80s-96 35.9-96 80v7.6c0 4.4-3.6 8-8 8h-48c-4.4 0-8-3.6-8-8V420c0-39.3 17.2-76 48.4-103.3C430.4 290.4 470 276 512 276s81.6 14.5 111.6 40.7C654.8 344 672 380.7 672 420c0 57.8-38.1 109.8-97.1 132.5z"}}]},name:"question-circle",theme:"filled"},Z=y,r=t(27029),q=function(o,i){return s.createElement(r.Z,(0,n.Z)((0,n.Z)({},o),{},{ref:i,icon:Z}))};q.displayName="QuestionCircleFilled";var L=s.forwardRef(q),e=t(85893),V=function(o){var i=o.name,m=o.children,g=function(j){return(0,e.jsx)(ee.Z,{checked:j.value,onChange:function(k){j.onChange(k.target.checked)},children:m})};return(0,e.jsx)(h.gN,{name:i,component:[g]})},te=t(47673),ne=t(4107),$=function(o){var i=o.name,m=o.children,g=o.style,B=o.display,j=function(k){return(0,e.jsx)(ne.Z,{value:k.value,style:g,onChange:function(I){k.onChange(I.target.value)},children:m})};return(0,e.jsx)(h.gN,{name:i,component:[j],display:B})},Ce=t(71194),D=t(50146),le=t(7354),w=t(54531),Q=t(11628),pe=function(o){var i=o.value,m=o.onChange,g=(0,s.useState)(!1),B=(0,T.Z)(g,2),j=B[0],S=B[1],k=(0,Q.IE)(),z=(0,s.useRef)(null);return(0,e.jsxs)(e.Fragment,{children:[j&&(0,e.jsx)(D.Z,{visible:!0,onCancel:function(){return S(!1)},bodyStyle:{maxHeight:600,overflowY:"auto",padding:0},maskClosable:!1,width:650,onOk:function(){var U;m((0,w.ZN)((U=z.current)===null||U===void 0?void 0:U.getValues())),S(!1)},children:k.schema?(0,e.jsx)(le.Z,{ref:z,initialValues:i,schema:k.schema,footerVisible:!1,onSubmit:function(U){console.log(U)}}):"\u95EE\u5377\u4E3A\u7A7A"}),(0,e.jsx)(G.Z,{onClick:function(){return S(!0)},style:{marginLeft:10},type:"dashed",children:i?"\u70B9\u51FB\u4FEE\u6539":"\u70B9\u51FB\u8BBE\u7F6E"})]})},fe=function(o){var i=o.name,m=o.children;return(0,e.jsx)(h.gN,{name:i,component:[pe]})},ge=t(17758),Pe=function(o){var i=o.value,m=o.onChange,g=(0,s.useState)(!1),B=(0,T.Z)(g,2),j=B[0],S=B[1];return(0,e.jsxs)(e.Fragment,{children:[j&&(0,e.jsx)(ge.cM,{value:i,title:"\u63D0\u4EA4\u540E\u56FE\u6587\u5C55\u793A",width:750,onChange:function(z){m(z),S(!1)},onClose:function(){S(!1)}}),(0,e.jsx)(G.Z,{onClick:function(){return S(!0)},style:{marginLeft:10},type:"dashed",children:i?"\u70B9\u51FB\u4FEE\u6539":"\u70B9\u51FB\u8BBE\u7F6E"})]})},Le=function(o){var i=o.name,m=o.children;return(0,e.jsx)(h.gN,{name:i,component:[Pe]})},Ae=t(64031),ye=(0,h.Pi)(function(){var v,o,i=(0,Q.IE)(),m=(0,s.useMemo)(function(){return(0,Ae.Np)({initialValues:i.setting})},[i.setting]),g=(0,s.useState)(!!((v=i.setting)!==null&&v!==void 0&&v.answerSetting.password)),B=(0,T.Z)(g,2),j=B[0],S=B[1],k=(0,s.useState)(!!((o=i.setting)!==null&&o!==void 0&&o.answerSetting.initialValues)),z=(0,T.Z)(k,2),I=z[0],U=z[1];return(0,s.useEffect)(function(){var A;S(!!((A=i.setting)!==null&&A!==void 0&&A.answerSetting.password))},[i.setting]),(0,s.useEffect)(function(){m.setFieldState("answerSetting.password",function(A){return A.display=j?"visible":"none"})},[j,m]),(0,s.useEffect)(function(){m.setFieldState("answerSetting.initialValues",function(A){return A.display=I?"visible":"none"})},[I,m]),(0,e.jsx)(h.RV,{form:m,children:(0,e.jsx)(h.Wo,{name:"answerSetting",children:(0,e.jsx)("div",{children:(0,e.jsx)(H.Z,{title:"\u586B\u5199\u8BBE\u7F6E",children:(0,e.jsxs)(W.Z,{gutter:[10,15],children:[(0,e.jsxs)(E.Z,{span:24,children:[(0,e.jsx)(ee.Z,{checked:j,onChange:function(O){S(O.target.checked)},children:"\u51ED\u5BC6\u7801\u586B\u5199"}),(0,e.jsx)(N.Z,{overlay:"\u53EA\u6709\u8F93\u5165\u5BC6\u7801\u624D\u80FD\u586B\u5199\u95EE\u5377",children:(0,e.jsx)(L,{className:"setting-prompt"})}),(0,e.jsx)($,{style:{width:200,marginLeft:20},name:"password"})]}),(0,e.jsxs)(E.Z,{span:24,children:[(0,e.jsx)(V,{name:"autoSave",children:"\u5F00\u542F\u81EA\u52A8\u6682\u5B58"}),(0,e.jsx)(N.Z,{overlay:"\u52FE\u9009\u540E\uFF0C\u53EF\u4EE5\u81EA\u52A8\u4FDD\u5B58\u672C\u6B21\u672A\u63D0\u4EA4\u7684\u586B\u5199\u5185\u5BB9\uFF0C\u518D\u6B21\u6253\u5F00\u95EE\u5377\u53EF\u663E\u793A\u4E0A\u6B21\u672A\u63D0\u4EA4\u7684\u586B\u5199\u5185\u5BB9\u3002",children:(0,e.jsx)(L,{className:"setting-prompt"})})]}),(0,e.jsxs)(E.Z,{span:24,children:[(0,e.jsx)(V,{name:"loginRequired",children:"\u9700\u8981\u767B\u5F55"}),(0,e.jsx)(N.Z,{overlay:"\u52FE\u9009\u540E\uFF0C\u53EA\u6709\u767B\u5F55\u7528\u6237\u624D\u53EF\u4EE5\u586B\u5199\u95EE\u5377\u3002",children:(0,e.jsx)(L,{className:"setting-prompt"})})]}),(0,e.jsxs)(E.Z,{span:24,children:[(0,e.jsx)(V,{name:"questionNumber",children:"\u663E\u793A\u9898\u76EE\u5E8F\u53F7"}),(0,e.jsx)(N.Z,{overlay:"\u52FE\u9009\u540E\uFF0C\u95EE\u5377\u4E2D\u7684\u9898\u76EE\u4F1A\u6309\u6392\u5217\u987A\u5E8F\u4ECE1\u5F00\u59CB\u663E\u793A\u9898\u76EE\u5E8F\u53F7\u3002",children:(0,e.jsx)(L,{className:"setting-prompt"})})]}),(0,e.jsxs)(E.Z,{span:24,children:[(0,e.jsx)(V,{name:"progressBar",children:"\u663E\u793A\u8FDB\u5EA6\u6761"}),(0,e.jsx)(N.Z,{overlay:"\u52FE\u9009\u540E\uFF0C\u586B\u5199\u8005\u6ED1\u52A8\u9875\u9762\u53EF\u4EE5\u770B\u5230\u5F53\u524D\u95EE\u5377\u586B\u5199\u8FDB\u5EA6\u3002",children:(0,e.jsx)(L,{className:"setting-prompt"})})]}),(0,e.jsxs)(E.Z,{span:24,children:[(0,e.jsx)(V,{name:"enableUpdate",children:"\u5141\u8BB8\u4FEE\u6539\u7B54\u6848"}),(0,e.jsx)(N.Z,{overlay:"\u52FE\u9009\u540E\uFF0C\u518D\u6B21\u6253\u5F00\u95EE\u5377\u4F1A\u56DE\u663E\u4E4B\u524D\u586B\u5199\u7684\u7B54\u6848\uFF0C\u5E76\u4E14\u53EF\u4EE5\u4FEE\u6539\u3002",children:(0,e.jsx)(L,{className:"setting-prompt"})})]}),(0,e.jsxs)(E.Z,{span:24,children:[(0,e.jsx)(ee.Z,{checked:I,onChange:function(O){U(O.target.checked)},children:"\u8BBE\u7F6E\u95EE\u5377\u9ED8\u8BA4\u7B54\u6848"}),(0,e.jsx)(N.Z,{overlay:"\u8BBE\u7F6E\u7684\u7B54\u6848\u5C06\u4F5C\u4E3A\u9ED8\u8BA4\u7B54\u6848\u5E26\u5165\u95EE\u5377",children:(0,e.jsx)(L,{className:"setting-prompt"})}),(0,e.jsx)(fe,{name:"initialValues"})]}),(0,e.jsxs)(E.Z,{span:24,children:[(0,e.jsx)(me.Z,{}),(0,e.jsx)(G.Z,{type:"primary",onClick:function(){i.updateSetting((0,w.ZN)(m.values)).then(function(O){O.success&&ke.default.success("\u8BBE\u7F6E\u4FDD\u5B58\u6210\u529F")})},children:"\u4FDD\u5B58\u8BBE\u7F6E"})]})]})})})})})}),Oe=t(54421),ue=t(38272),ce=t(94233),je=t(51890),p=t(49111),d=t(19650),l=t(71153),u=t(60331),x=t(62999),f=t(51753),C=t(86582),c=t(43358),F=t(97268),P=t(18106),de=t(67164),K=t(68489),xe=t(60780),Se=t.n(xe),qe=t(49101),De=F.Z.Option,Ee=(0,h.Pi)(function(v){var o=v.onChange,i=v.orgTreeData,m=v.positions,g=(0,s.useState)(),B=(0,T.Z)(g,2),j=B[0],S=B[1],k=(0,s.useState)(),z=(0,T.Z)(k,2),I=z[0],U=z[1];return(0,e.jsxs)(e.Fragment,{children:[(0,e.jsxs)(W.Z,{gutter:10,children:[(0,e.jsx)(E.Z,{span:16,children:(0,e.jsx)(f.Z,{onChange:function(O){return S(O)},style:{width:"100%"},dropdownStyle:{maxHeight:400,overflow:"auto"},treeData:[{title:"\u53D1\u8D77\u4EBA\u5F53\u524D\u90E8\u95E8",value:"${currentOrgId}",key:"${currentOrgId}"},{title:"\u53D1\u8D77\u4EBA\u4E0A\u7EA7\u90E8\u95E8",value:"${parentOrgId}",key:"${parentOrgId}"}].concat((0,C.Z)(i)),placeholder:"\u8BF7\u9009\u62E9\u673A\u6784",treeDefaultExpandAll:!0,allowClear:!0,value:j})}),(0,e.jsx)(E.Z,{span:8,children:(0,e.jsx)(F.Z,{style:{width:"100%"},placeholder:"\u8BF7\u9009\u62E9\u5C97\u4F4D",allowClear:!0,value:I,onChange:function(O){return U(O)},children:m.map(function(A){return(0,e.jsx)(De,{value:A.id,children:A.name},A.id)})})})]}),(0,e.jsx)(W.Z,{style:{marginTop:10},children:(0,e.jsx)(E.Z,{children:(0,e.jsx)(G.Z,{icon:(0,e.jsx)(qe.Z,{}),onClick:function(){(j||I)&&o("P:".concat(j||"",":").concat(I||""))},children:"\u6DFB\u52A0"})})})]})}),be=de.Z.TabPane,Ze=F.Z.Option,ve=(0,h.Pi)(function(v){var o=v.onChange,i=v.tabs,m=i===void 0?["user","role","position"]:i,g=(0,K.m2)(),B=(0,s.useState)(v.value||[]),j=(0,T.Z)(B,2),S=j[0],k=j[1];(0,s.useEffect)(function(){k(v.value||[])},[v.value]);var z=(0,K.LF)(S),I=g.depts,U=g.users,A=g.roles,O=g.positions,Ge=(0,s.useState)(),He=(0,T.Z)(Ge,2),ze=He[0],gt=He[1];(0,s.useEffect)(function(){ze&&g.loadUsers({orgId:ze,pageSize:1024})},[ze,g]);var Ye=(0,s.useMemo)(function(){return Se()(I.map(function(b){return{value:b.id,title:b.name,key:b.id,parentId:b.parentId}}),{parentProperty:"parentId",customID:"value"})},[I]),Ue=function(R){var _=(0,C.Z)(S);R.forEach(function(Y){_.includes(Y)||_.push(Y)}),k(_),o(_)},Ve=function(){for(var R=arguments.length,_=new Array(R),Y=0;Y<R;Y++)_[Y]=arguments[Y];var Ne=(0,C.Z)(S.filter(function(Xe){return!_.includes(Xe)}));k(Ne),o(Ne)};return(0,e.jsx)("div",{style:{width:400},children:(0,e.jsxs)(de.Z,{defaultActiveKey:"user",children:[(0,e.jsx)(be,{tab:"\u6210\u5458",disabled:!m.includes("user"),children:(0,e.jsxs)("div",{style:{height:400,overflowY:"auto",overflowX:"hidden"},children:[(0,e.jsxs)(W.Z,{gutter:20,children:[(0,e.jsx)(E.Z,{span:16,children:(0,e.jsx)(f.Z,{onChange:function(R){return gt(R)},style:{width:"100%"},dropdownStyle:{maxHeight:400,overflow:"auto"},treeData:Ye,placeholder:"\u8BF7\u9009\u62E9\u673A\u6784",treeDefaultExpandAll:!0,allowClear:!0})}),(0,e.jsx)(E.Z,{span:8,children:(0,e.jsx)(F.Z,{style:{width:"100%"},placeholder:"\u8BF7\u9009\u62E9\u6210\u5458",allowClear:!0,showSearch:!0,mode:"multiple",onChange:function(R){return Ue(R)},maxTagCount:0,value:S.filter(function(b){return b.startsWith("U:")}),dropdownRender:function(R){return(0,e.jsxs)("div",{children:[R,(0,e.jsx)(me.Z,{style:{margin:"4px 0"}}),(0,e.jsx)("div",{style:{display:"flex",flexWrap:"nowrap",padding:8},children:(0,e.jsx)(G.Z,{size:"small",type:"link",onClick:function(){return Ue(U.map(function(Y){return"U:".concat(Y.id)}))},children:"\u5168\u9009"})})]})},children:U.map(function(b){return(0,e.jsx)(Ze,{value:"U:".concat(b.id),children:b.name},b.id)})})})]}),(0,e.jsx)(d.Z,{style:{marginTop:20},wrap:!0,children:S.filter(function(b){return b.startsWith("U:")}).map(function(b){return(0,e.jsx)(u.Z,{closable:!0,color:"blue",onClose:function(){return Ve(b)},children:z.user[b.split(":")[1]]},b)})})]})},"user"),(0,e.jsx)(be,{tab:"\u89D2\u8272",disabled:!m.includes("role"),children:(0,e.jsxs)("div",{style:{height:400,overflowY:"auto",overflowX:"hidden"},children:[(0,e.jsx)(F.Z,{style:{width:"100%"},placeholder:"\u8BF7\u9009\u62E9\u89D2\u8272",allowClear:!0,showSearch:!0,mode:"multiple",value:S.filter(function(b){return b.startsWith("R:")}),onChange:function(R){return Ue(R)},maxTagCount:0,children:A.map(function(b){return(0,e.jsx)(Ze,{value:"R:".concat(b.id),children:b.name},b.id)})}),(0,e.jsx)(d.Z,{style:{marginTop:20},wrap:!0,children:S.filter(function(b){return b.startsWith("R:")}).map(function(b){return(0,e.jsx)(u.Z,{closable:!0,color:"blue",onClose:function(){return Ve(b)},children:z.role[b.split(":")[1]]},b)})})]})},"role"),(0,e.jsx)(be,{tab:"\u673A\u6784\u5C97\u4F4D",disabled:!m.includes("position"),children:(0,e.jsxs)("div",{style:{height:400,overflowY:"auto",overflowX:"hidden"},children:[(0,e.jsx)(Ee,{positions:O,orgTreeData:Ye,onChange:function(R){return Ue([R])}}),(0,e.jsx)(d.Z,{style:{marginTop:20},wrap:!0,children:S.filter(function(b){return b.startsWith("P:")}).map(function(b){var R=b.split(":"),_=(0,T.Z)(R,3),Y=_[1],Ne=_[2];return(0,e.jsxs)(u.Z,{closable:!0,color:"blue",onClose:function(){return Ve(b)},children:[z.org[Y],"-",z.position[Ne]]},Y+":"+Ne)})})]})},"position")]})})}),ae=ve,re=function(o){var i=(0,s.useState)([]),m=(0,T.Z)(i,2),g=m[0],B=m[1];return(0,e.jsx)(D.Z,{title:"\u8BBE\u7F6E\u534F\u4F5C\u7BA1\u7406\u5458",visible:!0,onCancel:o.onCancel,onOk:function(){return o.onOk(g)},children:(0,e.jsx)(ae,{tabs:["user"],onChange:function(S){B(S.map(function(k){return k.split(":")[1]}))}})})},Fe=function(){var o=(0,Q.IE)(),i=o.id,m=(0,s.useState)([]),g=(0,T.Z)(m,2),B=g[0],j=g[1],S=(0,s.useState)(!1),k=(0,T.Z)(S,2),z=k[0],I=k[1],U=function(){return K.hi.getProjectPartner(i).then(function(O){O&&j(O)})};return(0,s.useEffect)(function(){U()},[]),(0,e.jsxs)("div",{children:[(0,e.jsx)(H.Z,{title:(0,e.jsxs)(e.Fragment,{children:["\u534F\u4F5C\u7BA1\u7406\u5458\u5217\u8868",(0,e.jsx)(N.Z,{overlay:"\u534F\u4F5C\u8005\u53EF\u4EE5\u534F\u52A9\u521B\u5EFA\u8005\u8FDB\u884C\u7F16\u8F91\u95EE\u5377\u3001\u7BA1\u7406\u6570\u636E\u7B49\u64CD\u4F5C\u3002",children:(0,e.jsx)(L,{className:"setting-prompt",style:{marginLeft:5}})})]}),extra:(0,e.jsx)("a",{href:"#",onClick:function(){return I(!0)},children:"\u8BBE\u7F6E\u534F\u4F5C\u7BA1\u7406\u5458"}),children:(0,e.jsx)(ue.ZP,{itemLayout:"horizontal",dataSource:B,renderItem:function(O){return(0,e.jsxs)(ue.ZP.Item,{actions:O.type!==1?[(0,e.jsx)("a",{onClick:function(){D.Z.confirm({title:"\u5220\u9664\u534F\u4F5C\u8005",content:(0,e.jsxs)("div",{children:["\u786E\u5B9A\u5220\u9664\u534F\u4F5C\u8005 ",(0,e.jsx)("b",{children:O.user.name}),"\u5417\uFF1F"]}),onOk:function(){K.hi.deleteProjectPartner(O.id,i).then(function(ze){ze.success&&U()})}})},children:"\u5220\u9664"},"list-loadmore-edit")]:[],children:[(0,e.jsx)(ue.ZP.Item.Meta,{avatar:(0,e.jsx)(je.C,{src:O.user.avatar?"/api/public/preview/".concat(O.user.avatar):""}),title:O.user.name}),(0,e.jsx)("div",{children:O.type===1?"\u521B\u5EFA\u8005":"\u534F\u4F5C\u8005"})]})}})}),z&&(0,e.jsx)(re,{onCancel:function(){return I(!1)},onOk:function(O){K.hi.addProjectPartner({userIds:O,projectId:i}).then(function(){U(),I(!1)})}})]})},ie=t(11849),Je=(0,h.Pi)(function(){var v,o,i=(0,Q.IE)(),m=(0,s.useMemo)(function(){return(0,Ae.Np)({initialValues:i.setting})},[i.setting]),g=(0,s.useState)(!!((v=i.setting)!==null&&v!==void 0&&(o=v.submittedSetting)!==null&&o!==void 0&&o.contentHtml)),B=(0,T.Z)(g,2),j=B[0],S=B[1];return(0,s.useEffect)(function(){m.setFieldState("submittedSetting.contentHtml",function(k){return k.display=j?"visible":"none"})},[j,m]),(0,e.jsx)(h.RV,{form:m,children:(0,e.jsx)(h.Wo,{name:"submittedSetting",children:(0,e.jsx)("div",{children:(0,e.jsx)(H.Z,{title:"\u586B\u5199\u8005\u63D0\u4EA4\u95EE\u5377\u540E",children:(0,e.jsxs)(W.Z,{gutter:[10,15],children:[(0,e.jsxs)(E.Z,{span:24,children:[(0,e.jsx)(ee.Z,{checked:j,onChange:function(z){S(z.target.checked)},children:"\u63D0\u4EA4\u540E\u56FE\u6587\u5C55\u793A"}),(0,e.jsx)(N.Z,{overlay:"\u4F60\u53EF\u4EE5\u5728\u8868\u5355\u63D0\u4EA4\u9875\u9762\u8BBE\u7F6E\u66F4\u4E3A\u4E30\u5BCC\u591A\u5F69\u7684\u5185\u5BB9\uFF0C\u5305\u62EC\u63D2\u5165\u56FE\u7247\u3001\u8BBE\u7F6E\u5B57\u53F7\u3001\u5B57\u4F53\u989C\u8272\u3001\u5E8F\u53F7\u3001\u8BBE\u7F6E\u8D85\u94FE\u63A5\u7B49\u7B49\u3002 ",children:(0,e.jsx)(L,{className:"setting-prompt"})}),(0,e.jsx)(Le,{name:"contentHtml"})]}),(0,e.jsxs)(E.Z,{span:24,children:[(0,e.jsx)(me.Z,{}),(0,e.jsx)(G.Z,{type:"primary",onClick:function(){i.updateSetting((0,ie.Z)((0,ie.Z)({},i.setting),(0,w.ZN)(m.values))).then(function(z){z.success&&ke.default.success("\u8BBE\u7F6E\u4FDD\u5B58\u6210\u529F")})},children:"\u4FDD\u5B58\u8BBE\u7F6E"})]})]})})})})})}),_e=(0,h.Pi)(function(){var v=(0,s.useContext)(a),o=v.settingStore,i=o.activePanel,m=function(){if(i==="answerSetting")return(0,e.jsx)(ye,{});if(i==="memberSetting")return(0,e.jsx)(Fe,{});if(i==="submittedSetting")return(0,e.jsx)(Je,{})},g=M("-content");return(0,e.jsx)("div",{className:g,children:m()})}),xt=t(7359),$e=t(27279),et=t(8212),tt={icon:{tag:"svg",attrs:{viewBox:"64 64 896 896",focusable:"false"},children:[{tag:"path",attrs:{d:"M824.2 699.9a301.55 301.55 0 00-86.4-60.4C783.1 602.8 812 546.8 812 484c0-110.8-92.4-201.7-203.2-200-109.1 1.7-197 90.6-197 200 0 62.8 29 118.8 74.2 155.5a300.95 300.95 0 00-86.4 60.4C345 754.6 314 826.8 312 903.8a8 8 0 008 8.2h56c4.3 0 7.9-3.4 8-7.7 1.9-58 25.4-112.3 66.7-153.5A226.62 226.62 0 01612 684c60.9 0 118.2 23.7 161.3 66.8C814.5 792 838 846.3 840 904.3c.1 4.3 3.7 7.7 8 7.7h56a8 8 0 008-8.2c-2-77-33-149.2-87.8-203.9zM612 612c-34.2 0-66.4-13.3-90.5-37.5a126.86 126.86 0 01-37.5-91.8c.3-32.8 13.4-64.5 36.3-88 24-24.6 56.1-38.3 90.4-38.7 33.9-.3 66.8 12.9 91 36.6 24.8 24.3 38.4 56.8 38.4 91.4 0 34.2-13.3 66.3-37.5 90.5A127.3 127.3 0 01612 612zM361.5 510.4c-.9-8.7-1.4-17.5-1.4-26.4 0-15.9 1.5-31.4 4.3-46.5.7-3.6-1.2-7.3-4.5-8.8-13.6-6.1-26.1-14.5-36.9-25.1a127.54 127.54 0 01-38.7-95.4c.9-32.1 13.8-62.6 36.3-85.6 24.7-25.3 57.9-39.1 93.2-38.7 31.9.3 62.7 12.6 86 34.4 7.9 7.4 14.7 15.6 20.4 24.4 2 3.1 5.9 4.4 9.3 3.2 17.6-6.1 36.2-10.4 55.3-12.4 5.6-.6 8.8-6.6 6.3-11.6-32.5-64.3-98.9-108.7-175.7-109.9-110.9-1.7-203.3 89.2-203.3 199.9 0 62.8 28.9 118.8 74.2 155.5-31.8 14.7-61.1 35-86.5 60.4-54.8 54.7-85.8 126.9-87.8 204a8 8 0 008 8.2h56.1c4.3 0 7.9-3.4 8-7.7 1.9-58 25.4-112.3 66.7-153.5 29.4-29.4 65.4-49.8 104.7-59.7 3.9-1 6.5-4.7 6-8.7z"}}]},name:"team",theme:"outlined"},nt=tt,we=function(o,i){return s.createElement(r.Z,(0,n.Z)((0,n.Z)({},o),{},{ref:i,icon:nt}))};we.displayName="TeamOutlined";var lt=s.forwardRef(we),at={icon:{tag:"svg",attrs:{viewBox:"0 0 1024 1024",focusable:"false"},children:[{tag:"path",attrs:{d:"M512 64L128 192v384c0 212.1 171.9 384 384 384s384-171.9 384-384V192L512 64zm312 512c0 172.3-139.7 312-312 312S200 748.3 200 576V246l312-110 312 110v330z"}},{tag:"path",attrs:{d:"M378.4 475.1a35.91 35.91 0 00-50.9 0 35.91 35.91 0 000 50.9l129.4 129.4 2.1 2.1a33.98 33.98 0 0048.1 0L730.6 434a33.98 33.98 0 000-48.1l-2.8-2.8a33.98 33.98 0 00-48.1 0L483 579.7 378.4 475.1z"}}]},name:"safety",theme:"outlined"},rt=at,Qe=function(o,i){return s.createElement(r.Z,(0,n.Z)((0,n.Z)({},o),{},{ref:i,icon:rt}))};Qe.displayName="SafetyOutlined";var it=s.forwardRef(Qe),ot=t(25782),st=t(94184),ut=t.n(st),Ke=$e.Z.Panel,ct=[{key:"answerSetting",title:"\u6570\u636E\u6536\u96C6\u8BBE\u7F6E",icon:(0,e.jsx)(et.Z,{}),description:"\u57FA\u672C\u7684\u95EE\u5377\u586B\u5199\u8BBE\u7F6E"},{key:"memberSetting",title:"\u534F\u540C\u7F16\u8F91",icon:(0,e.jsx)(lt,{}),description:"\u8BBE\u7F6E\u534F\u4F5C\u7BA1\u7406\u5458"},{key:"submittedSetting",title:"\u63D0\u4EA4\u95EE\u5377\u6570\u636E\u540E",icon:(0,e.jsx)(it,{}),description:"\u95EE\u5377\u63D0\u4EA4\u5B8C\u6210\u9875\u9762\u8BBE\u7F6E"}],dt=(0,h.Pi)(function(){var v=M("-nav"),o=(0,s.useContext)(a),i=o.settingStore,m=i.activePanel;return(0,e.jsx)("div",{className:v,children:(0,e.jsxs)($e.Z,{accordion:!0,expandIcon:function(B){var j=B.isActive;return(0,e.jsx)(ot.Z,{rotate:j?90:0})},defaultActiveKey:"1",children:[(0,e.jsx)(Ke,{header:"\u57FA\u7840\u8BBE\u7F6E",style:{padding:0},children:ct.map(function(g){return(0,e.jsxs)(W.Z,{className:ut()("nav-panel-item",{active:m===g.key}),onClick:function(){return i.activePanel=g.key},children:[(0,e.jsx)(E.Z,{span:4,children:(0,e.jsx)(je.C,{icon:g.icon,shape:"square"})}),(0,e.jsx)(E.Z,{span:20,children:(0,e.jsxs)(W.Z,{children:[(0,e.jsx)(E.Z,{span:24,children:g.title}),(0,e.jsx)(E.Z,{span:24,children:g.description})]})})]},g.key)})},"1"),(0,e.jsx)(Ke,{header:"\u9AD8\u7EA7\u8BBE\u7F6E",children:(0,e.jsx)("p",{children:"todo"})},"2")]})})}),vt=t(69610),ft=t(54941),bt=function(){function v(o){(0,vt.Z)(this,v),this.rootStore=void 0,this.activePanel=void 0,this.rootStore=o,this.activePanel="answerSetting",this.makeObservable()}return(0,ft.Z)(v,[{key:"makeObservable",value:function(){(0,w.Ou)(this,{rootStore:w.LO.ref,activePanel:w.LO.ref})}}]),v}(),ht=t(77613),mt=t(27400),We=(0,ht.P)(function(){var v=(0,Q.IE)(),o=(0,mt.a)(),i=o.isMobile,m=(0,s.useMemo)(function(){return new bt(v)},[v]);return(0,e.jsx)(a.Provider,{value:{prefixCls:"survey-setting",settingStore:m},children:(0,e.jsxs)("div",{className:"survey-setting",children:[!i&&(0,e.jsx)(dt,{}),(0,e.jsx)(_e,{})]})})}),pt=We},11628:function(Be,X,t){"use strict";t.d(X,{xI:function(){return s},Ge:function(){return J},IE:function(){return M}});var h=t(67294),s=(0,h.createContext)({}),a=s.Provider;function M(){var H=(0,h.useContext)(s);return H.store}function J(){var H=M();return H.flowStore}},39144:function(Be,X,t){"use strict";t.d(X,{Z:function(){return T}});var h=t(96156),s=t(22122),a=t(67294),M=t(94184),J=t.n(M),H=t(98423),oe=t(65632),W=function(n,y){var Z={};for(var r in n)Object.prototype.hasOwnProperty.call(n,r)&&y.indexOf(r)<0&&(Z[r]=n[r]);if(n!=null&&typeof Object.getOwnPropertySymbols=="function")for(var q=0,r=Object.getOwnPropertySymbols(n);q<r.length;q++)y.indexOf(r[q])<0&&Object.prototype.propertyIsEnumerable.call(n,r[q])&&(Z[r[q]]=n[r[q]]);return Z},he=function(y){var Z=y.prefixCls,r=y.className,q=y.hoverable,L=q===void 0?!0:q,e=W(y,["prefixCls","className","hoverable"]);return a.createElement(oe.C,null,function(V){var te=V.getPrefixCls,ne=te("card",Z),$=J()("".concat(ne,"-grid"),r,(0,h.Z)({},"".concat(ne,"-grid-hoverable"),L));return a.createElement("div",(0,s.Z)({},e,{className:$}))})},G=he,Ie=function(n,y){var Z={};for(var r in n)Object.prototype.hasOwnProperty.call(n,r)&&y.indexOf(r)<0&&(Z[r]=n[r]);if(n!=null&&typeof Object.getOwnPropertySymbols=="function")for(var q=0,r=Object.getOwnPropertySymbols(n);q<r.length;q++)y.indexOf(r[q])<0&&Object.prototype.propertyIsEnumerable.call(n,r[q])&&(Z[r[q]]=n[r[q]]);return Z},ke=function(y){return a.createElement(oe.C,null,function(Z){var r=Z.getPrefixCls,q=y.prefixCls,L=y.className,e=y.avatar,V=y.title,te=y.description,ne=Ie(y,["prefixCls","className","avatar","title","description"]),$=r("card",q),Ce=J()("".concat($,"-meta"),L),D=e?a.createElement("div",{className:"".concat($,"-meta-avatar")},e):null,le=V?a.createElement("div",{className:"".concat($,"-meta-title")},V):null,w=te?a.createElement("div",{className:"".concat($,"-meta-description")},te):null,Q=le||w?a.createElement("div",{className:"".concat($,"-meta-detail")},le,w):null;return a.createElement("div",(0,s.Z)({},ne,{className:Ce}),D,Q)})},Te=ke,me=t(67164),se=t(71230),E=t(15746),Me=t(97647),N=function(n,y){var Z={};for(var r in n)Object.prototype.hasOwnProperty.call(n,r)&&y.indexOf(r)<0&&(Z[r]=n[r]);if(n!=null&&typeof Object.getOwnPropertySymbols=="function")for(var q=0,r=Object.getOwnPropertySymbols(n);q<r.length;q++)y.indexOf(r[q])<0&&Object.prototype.propertyIsEnumerable.call(n,r[q])&&(Z[r[q]]=n[r[q]]);return Z};function Re(n){var y=n.map(function(Z,r){return a.createElement("li",{style:{width:"".concat(100/n.length,"%")},key:"action-".concat(r)},a.createElement("span",null,Z))});return y}var ee=a.forwardRef(function(n,y){var Z,r,q=a.useContext(oe.E_),L=q.getPrefixCls,e=q.direction,V=a.useContext(Me.Z),te=function(Fe){var ie;(ie=n.onTabChange)===null||ie===void 0||ie.call(n,Fe)},ne=function(){var Fe;return a.Children.forEach(n.children,function(ie){ie&&ie.type&&ie.type===G&&(Fe=!0)}),Fe},$=n.prefixCls,Ce=n.className,D=n.extra,le=n.headStyle,w=le===void 0?{}:le,Q=n.bodyStyle,pe=Q===void 0?{}:Q,fe=n.title,ge=n.loading,Pe=n.bordered,Le=Pe===void 0?!0:Pe,Ae=n.size,ye=n.type,Oe=n.cover,ue=n.actions,ce=n.tabList,je=n.children,p=n.activeTabKey,d=n.defaultActiveTabKey,l=n.tabBarExtraContent,u=n.hoverable,x=n.tabProps,f=x===void 0?{}:x,C=N(n,["prefixCls","className","extra","headStyle","bodyStyle","title","loading","bordered","size","type","cover","actions","tabList","children","activeTabKey","defaultActiveTabKey","tabBarExtraContent","hoverable","tabProps"]),c=L("card",$),F=pe.padding===0||pe.padding==="0px"?{padding:24}:void 0,P=a.createElement("div",{className:"".concat(c,"-loading-block")}),de=a.createElement("div",{className:"".concat(c,"-loading-content"),style:F},a.createElement(se.Z,{gutter:8},a.createElement(E.Z,{span:22},P)),a.createElement(se.Z,{gutter:8},a.createElement(E.Z,{span:8},P),a.createElement(E.Z,{span:15},P)),a.createElement(se.Z,{gutter:8},a.createElement(E.Z,{span:6},P),a.createElement(E.Z,{span:18},P)),a.createElement(se.Z,{gutter:8},a.createElement(E.Z,{span:13},P),a.createElement(E.Z,{span:9},P)),a.createElement(se.Z,{gutter:8},a.createElement(E.Z,{span:4},P),a.createElement(E.Z,{span:3},P),a.createElement(E.Z,{span:16},P))),K=p!==void 0,xe=(0,s.Z)((0,s.Z)({},f),(Z={},(0,h.Z)(Z,K?"activeKey":"defaultActiveKey",K?p:d),(0,h.Z)(Z,"tabBarExtraContent",l),Z)),Se,qe=ce&&ce.length?a.createElement(me.Z,(0,s.Z)({size:"large"},xe,{className:"".concat(c,"-head-tabs"),onChange:te}),ce.map(function(re){return a.createElement(me.Z.TabPane,{tab:re.tab,disabled:re.disabled,key:re.key})})):null;(fe||D||qe)&&(Se=a.createElement("div",{className:"".concat(c,"-head"),style:w},a.createElement("div",{className:"".concat(c,"-head-wrapper")},fe&&a.createElement("div",{className:"".concat(c,"-head-title")},fe),D&&a.createElement("div",{className:"".concat(c,"-extra")},D)),qe));var De=Oe?a.createElement("div",{className:"".concat(c,"-cover")},Oe):null,Ee=a.createElement("div",{className:"".concat(c,"-body"),style:pe},ge?de:je),be=ue&&ue.length?a.createElement("ul",{className:"".concat(c,"-actions")},Re(ue)):null,Ze=(0,H.Z)(C,["onTabChange"]),ve=Ae||V,ae=J()(c,(r={},(0,h.Z)(r,"".concat(c,"-loading"),ge),(0,h.Z)(r,"".concat(c,"-bordered"),Le),(0,h.Z)(r,"".concat(c,"-hoverable"),u),(0,h.Z)(r,"".concat(c,"-contain-grid"),ne()),(0,h.Z)(r,"".concat(c,"-contain-tabs"),ce&&ce.length),(0,h.Z)(r,"".concat(c,"-").concat(ve),ve),(0,h.Z)(r,"".concat(c,"-type-").concat(ye),!!ye),(0,h.Z)(r,"".concat(c,"-rtl"),e==="rtl"),r),Ce);return a.createElement("div",(0,s.Z)({ref:y},Ze,{className:ae}),Se,De,Ee,be)});ee.Grid=G,ee.Meta=Te;var T=ee},58024:function(Be,X,t){"use strict";var h=t(38663),s=t.n(h),a=t(70347),M=t.n(a),J=t(18106),H=t(13062),oe=t(89032)}}]);
