(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[1117],{90586:function(De,Y,t){"use strict";t.d(Y,{P:function(){return v}});var $=t(11849),w=t(93224),z=t(2299),C=t(67294),ie=t(30381),R=t.n(ie),Z=t(85893),v=function(X){var qe=X.children,V=X.syncDisplay,je=X.isMoment,H=(0,w.Z)(X,["children","syncDisplay","isMoment"]),he=(0,C.useMemo)(function(){return(0,z.$j)(function(E){var j=E.value,T=E.onChange,F=j;return je&&j&&(F=R()(j)),C.cloneElement(qe,{value:F,onChange:function(e){T(e)}})},(0,z.jM)(function(E,j){return{value:E.value,onChange:function(F){R().isMoment(F)?j.onInput(F.valueOf()):j.onInput(F)}}}))},[]);return(0,Z.jsx)(z.gN,(0,$.Z)((0,$.Z)({},H),{},{component:[he,X],reactions:[function(E){if(V)if(typeof V=="string"){var j=E.query(E.path.parent().concat(V)).get("value");j?E.display="visible":E.display="none"}else typeof V=="object"&&Object.keys(V).forEach(function(T){var F=E.query(E.path.parent().concat(T)).get("value");V[T].includes(F)?E.display="visible":E.display="none"})}]}))};Y.Z=v},17758:function(De,Y,t){"use strict";t.d(Y,{cM:function(){return ye},Bn:function(){return me},ZP:function(){return He}});var $=t(2824),w=t(20310),z=t(67294),C=t(71167),ie=t.n(C),R=t(29163),Z=t(94184),v=t.n(Z),ne=t(11849),X=t(71194),qe=t(50146),V=t(49111),je=t(19650),H=t(57663),he=t(71577),E=t(93224),j=t(76095),T=t.n(j),F=t(85551),Ne=t.n(F),e=t(69610),Ce=t(54941),W=t(63543),Ie=t(94663),Oe=t(43028),ze=t(5869),Te=T().import("blots/block"),ae=function(b){(0,Oe.Z)(l,b);var h=(0,ze.Z)(l);function l(){return(0,e.Z)(this,l),h.apply(this,arguments)}return(0,Ce.Z)(l,[{key:"deleteAt",value:function(x,f){(0,W.Z)((0,Ie.Z)(l.prototype),"deleteAt",this).call(this,x,f),this.cache={}}}],[{key:"create",value:function(x){var f=(0,W.Z)((0,Ie.Z)(l),"create",this).call(this,x);if(x===!0)return f;var y=document.createElement("img");return y.setAttribute("src",x),f.appendChild(y),f}},{key:"value",value:function(x){var f=x.dataset,y=f.src,p=f.custom;return{src:y,custom:p}}}]),l}(Te);ae.blotName="imageBlot",ae.className="image-uploading",ae.tagName="span",T().register({"formats/imageBlot":ae});var G=ae,Me=function(){function b(h,l){(0,e.Z)(this,b),this.quill=h,this.options=l,this.range=null,typeof this.options.upload!="function"&&console.warn("[Missing config] upload function that returns a promise is required");var s=this.quill.getModule("toolbar");s.addHandler("image",this.selectLocalImage.bind(this)),this.handleDrop=this.handleDrop.bind(this),this.handlePaste=this.handlePaste.bind(this),this.quill.root.addEventListener("drop",this.handleDrop,!1),this.quill.root.addEventListener("paste",this.handlePaste,!1)}return(0,Ce.Z)(b,[{key:"selectLocalImage",value:function(){var l=this;this.range=this.quill.getSelection(),this.fileHolder=document.createElement("input"),this.fileHolder.setAttribute("type","file"),this.fileHolder.setAttribute("accept","image/*"),this.fileHolder.setAttribute("style","visibility:hidden"),this.fileHolder.onchange=this.fileChanged.bind(this),document.body.appendChild(this.fileHolder),this.fileHolder.click(),window.requestAnimationFrame(function(){document.body.removeChild(l.fileHolder)})}},{key:"handleDrop",value:function(l){var s=this;if(l.stopPropagation(),l.preventDefault(),l.dataTransfer&&l.dataTransfer.files&&l.dataTransfer.files.length){if(document.caretRangeFromPoint){var x=document.getSelection(),f=document.caretRangeFromPoint(l.clientX,l.clientY);x&&f&&x.setBaseAndExtent(f.startContainer,f.startOffset,f.startContainer,f.startOffset)}else{var y=document.getSelection(),p=document.caretPositionFromPoint(l.clientX,l.clientY);y&&p&&y.setBaseAndExtent(p.offsetNode,p.offset,p.offsetNode,p.offset)}this.range=this.quill.getSelection();var A=l.dataTransfer.files[0];setTimeout(function(){s.range=s.quill.getSelection(),s.readAndUploadFile(A)},0)}}},{key:"handlePaste",value:function(l){var s=this,x=l.clipboardData||window.clipboardData;if(x&&(x.items||x.files))for(var f=x.items||x.files,y=/^image\/(jpe?g|gif|png|svg|webp)$/i,p=0;p<f.length;p++)y.test(f[p].type)&&function(){var A=f[p].getAsFile?f[p].getAsFile():f[p];A&&(s.range=s.quill.getSelection(),l.preventDefault(),setTimeout(function(){s.range=s.quill.getSelection(),s.readAndUploadFile(A)},0))}()}},{key:"readAndUploadFile",value:function(l){var s=this,x=!1,f=new FileReader;f.addEventListener("load",function(){if(!x){var y=f.result;s.insertBase64Image(y)}},!1),l&&f.readAsDataURL(l),this.options.upload(l).then(function(y){s.insertToEditor(y)},function(y){x=!0,s.removeBase64Image(),console.warn(y)})}},{key:"fileChanged",value:function(){var l=this.fileHolder.files[0];this.readAndUploadFile(l)}},{key:"insertBase64Image",value:function(l){var s=this.range;this.quill.insertEmbed(s.index,G.blotName,"".concat(l),"user")}},{key:"insertToEditor",value:function(l){var s=this.range;this.quill.deleteText(s.index,3,"user"),console.log(s,l),this.quill.insertEmbed(s.index,"image","".concat(l)),s.index++,this.quill.setSelection(s,"user")}},{key:"removeBase64Image",value:function(){var l=this.range;this.quill.deleteText(l.index,3,"user")}}]),b}();window.ImageUploader=Me;var fe=Me,Le=function(){function b(h,l){(0,e.Z)(this,b),this.quill=h,this.options=l,this.container=document.querySelector(l.container),h.on("text-change",this.update.bind(this)),this.update()}return(0,Ce.Z)(b,[{key:"calculate",value:function(){var l=this.quill.getText();return this.options.unit==="word"?(l=l.trim(),l.length>0?l.split(/\s+/).length:0):l.length}},{key:"update",value:function(){var l=this.calculate(),s=this.options.unit;l!==1&&(s+="s")}}]),b}();C.Quill.register("modules/counter",Le);var re=t(68489),P=t(85893);T().register("modules/imageResize",Ne()),T().register("modules/imageUploader",fe);var Ee=T().import("attributors/style/size");Ee.whitelist=["9px","10px","11px","12px","14px","16px","18px","20px","22px","24px","26px","36px"],T().register(Ee,!0);var we=function(h){var l=(0,z.useRef)(null),s=(0,z.useRef)(),x=(0,z.useRef)();return(0,z.useEffect)(function(){return l.current&&(s.current=new(T())(l.current,{theme:"snow",formats:["size","color","background","header","bold","italic","underline","strike","blockquote","list","bullet","indent","link","image","formula","align","imageBlot"],modules:{counter:{container:"#counter",unit:"word"},imageResize:{parchment:T().import("parchment"),modules:["Resize","DisplaySize"]},imageUploader:{upload:function(y){return new Promise(function(p,A){re.hi.uploadImage(3,y).then(function(M){M.success&&p("/api/public/preview/".concat(M.data.id))}).catch(function(M){A("Upload failed"),console.error("Error:",M)})})}},toolbar:[[{size:["9px","10px","11px","12px","14px","16px","18px","20px","22px","24px","26px","36px"]}],[{color:[]},{background:[]}],["bold","italic","underline","strike","blockquote"],[{align:""},{align:"center"},{align:"right"},{align:"justify"}],["link","image"],[{list:"ordered"},{list:"bullet"},{indent:"-1"},{indent:"+1"}]]}}),h.value&&s.current.setText(h.value),s.current.on("text-change",function(f,y,p){var A,M=(A=s.current)===null||A===void 0?void 0:A.root.innerHTML;M!==x.current&&h.onChange&&(h.onChange(M||""),x.current=M)})),function(){}},[]),(0,z.useEffect)(function(){var f,y=x.current,p=(f=h.value)!==null&&f!==void 0?f:"";if(p!==y){var A;x.current=p,(A=s.current)===null||A===void 0||A.setContents(s.current.clipboard.convert(p))}},[h.value]),(0,P.jsx)("div",{className:"custom-quill-editor",children:(0,P.jsx)("div",{ref:l})})},ye=function(h){var l=h.onChange,s=h.onClose,x=h.value,f=(0,E.Z)(h,["onChange","onClose","value"]),y=(0,z.useState)(x),p=(0,$.Z)(y,2),A=p[0],M=p[1];return(0,P.jsx)(qe.Z,(0,ne.Z)((0,ne.Z)({visible:!0,onCancel:s,footer:!1,maskClosable:!1,closable:!1,width:650},f),{},{children:(0,P.jsxs)("div",{children:[(0,P.jsx)(we,{value:A,onChange:function(se){M(se)}}),(0,P.jsx)("div",{style:{textAlign:"right",marginTop:20},children:(0,P.jsxs)(je.Z,{children:[(0,P.jsx)(he.Z,{onClick:s,children:"\u53D6\u6D88"}),(0,P.jsx)(he.Z,{type:"primary",onClick:function(){return l(A)},children:"\u4FDD\u5B58"})]})})]})}))},Re=t(8166),Se;window.katex=Re.Z;var J=C.Quill.import("attributors/style/size");J.whitelist=["9px","10px","11px","12px","14px","16px","18px","20px","22px","24px","26px","36px"],C.Quill.register(J,!0);var tt=C.Quill.import("modules/clipboard"),Ue=C.Quill.import("delta"),nt=null,_=R.ZP.div(Se||(Se=(0,w.Z)([`
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
`])),function(b){return b.size==="middle"?"6px 11px 4px 11px":b.size==="small"?"2px 2px 0px 5px":"14px 11px"},function(b){return b.hasError?"1px solid #ff4d4f":"1px solid transparent"},function(b){return b.hasError?"1px solid #ff4d4f !important":"1px solid #1890ff !important"},function(b){return b.innerStyle&&b.innerStyle.fontSize},function(b){return b.innerStyle&&b.innerStyle.textAlign});function Ve(b){return typeof b=="symbol"?"":b&&b.startsWith("<p")?b:"<p>".concat(b,"</p>")}var Ze=C.Quill.import("ui/icons");Ze.tags='<svg style="width:18px;height:18px;"  viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="512" height="512"><path d="M851.968 167.936l0 109.568-281.6 0 0 587.776-116.736 0 0-587.776-281.6 0 0-109.568 679.936 0z" p-id="3840"></path></svg>';var me=function(h){var l=h.onChange,s=h.value,x=s===void 0?"":s,f=h.placeholder,y=h.style,p=h.className,A=h.bounds,M=(0,z.useState)(!1),be=(0,$.Z)(M,2),se=be[0],Fe=be[1],ke=(0,z.useState)(!1),oe=(0,$.Z)(ke,2),Qe=oe[0],ue=oe[1],Q=function(ee){if(ee.startsWith("<p>")){var Be=ee.replace(/^<p>/,"").replace(/<\/p>$/,"");l(Be==="<br>"?"":Be)}else l(ee)};function $e(){ue(!0)}return(0,z.useEffect)(function(){setTimeout(function(){return Fe(!1)},50)},[]),(0,P.jsxs)(_,{style:y,className:v()(p,{"is-focus":se,"not-focus":!se}),innerStyle:h.innerStyle,size:h.size,children:[Qe&&(0,P.jsx)(ye,{onClose:function(){return ue(!1)},value:x,onChange:function(ee){ee&&Q(ee),ue(!1)}}),(0,P.jsx)(ie(),{theme:"bubble",value:Ve(x),onChange:Q,style:{textAlign:"center"},bounds:A,onFocus:function(){Fe(!0)},scrollingContainer:"body",className:p,onBlur:function(){Fe(!1)},placeholder:f,modules:(0,z.useMemo)(function(){return{toolbar:{container:[[{size:["9px","10px","11px","12px","14px","16px","18px","20px","22px","24px","26px","36px"]}],[{color:[]},{background:[]}],["bold","italic"],["clean","tags"]],handlers:{tags:$e}},clipboard:{matchVisual:!1}}},[])})]})},He=me},46839:function(De,Y,t){"use strict";t.r(Y),t.d(Y,{Setting:function(){return et},default:function(){return Et}});var $=t(13062),w=t(71230),z=t(89032),C=t(15746),ie=t(34792),R=t(48086),Z=t(2299),v=t(67294),ne=(0,v.createContext)({});function X(){var o=arguments.length>0&&arguments[0]!==void 0?arguments[0]:"",n=(0,v.useContext)(ne),r=n.prefixCls;return r+o}var qe=t(58024),V=t(39144),je=t(77576),H=t(12028),he=t(22385),E=t(31097),j=t(2824),T=t(99008),F=t(84387),Ne=t(63185),e=t(85893),Ce=function(n){var r=n.children,u=_objectWithoutProperties(n,["children"]),i=function(a){return _jsx(_Checkbox,{checked:a.value,onChange:function(q){a.onChange(q.target.checked)},children:r})};return _jsx(Field,_objectSpread({component:[i]},u))},W=t(11849),Ie=t(47673),Oe=t(4107),ze=t(93224),Te=function(n){var r=n.children,u=n.style,i=(0,ze.Z)(n,["children","style"]),d=function(m){return(0,e.jsx)(Oe.Z,{value:m.value,style:u,onChange:function(g){m.onChange(g.target.value)},children:r})};return(0,e.jsx)(Z.gN,(0,W.Z)({component:[d]},i))},ae=t(57663),G=t(71577),Me=t(71194),fe=t(50146),Le=t(7354),re=t(54531),P=t(11628),Ee=function(n){var r=n.value,u=n.onChange,i=(0,v.useState)(!1),d=(0,j.Z)(i,2),a=d[0],m=d[1],q=(0,P.IE)(),g=(0,v.useRef)(null);return(0,e.jsxs)(e.Fragment,{children:[a&&(0,e.jsx)(fe.Z,{visible:!0,onCancel:function(){return m(!1)},bodyStyle:{maxHeight:600,overflowY:"auto",padding:0},maskClosable:!1,width:650,onOk:function(){var D;u((0,re.ZN)((D=g.current)===null||D===void 0?void 0:D.getValues())),m(!1)},children:q.schema?(0,e.jsx)(Le.Z,{ref:g,initialValues:r,schema:q.schema,footerVisible:!1,onSubmit:function(D){console.log(D)}}):"\u95EE\u5377\u4E3A\u7A7A"}),(0,e.jsx)(G.Z,{onClick:function(){return m(!0)},style:{marginLeft:10},type:"dashed",children:r?"\u70B9\u51FB\u4FEE\u6539":"\u70B9\u51FB\u8BBE\u7F6E"})]})},we=function(n){var r=n.name,u=n.basePath;return(0,e.jsx)(Z.gN,{name:r,component:[Ee],basePath:u})},ye=t(17758),Re=function(n){var r=n.value,u=n.onChange,i=(0,v.useState)(!1),d=(0,j.Z)(i,2),a=d[0],m=d[1];return(0,e.jsxs)(e.Fragment,{children:[a&&(0,e.jsx)(ye.cM,{value:r,title:"\u63D0\u4EA4\u540E\u56FE\u6587\u5C55\u793A",width:750,onChange:function(g){u(g),m(!1)},onClose:function(){m(!1)}}),(0,e.jsx)(G.Z,{onClick:function(){return m(!0)},style:{marginLeft:10},type:"dashed",children:r?"\u70B9\u51FB\u4FEE\u6539":"\u70B9\u51FB\u8BBE\u7F6E"})]})},Se=function(n){var r=n.name,u=n.basePath;return(0,e.jsx)(Z.gN,{name:r,component:[Re],basePath:u})},J=function(n){var r=function(i){return(0,e.jsx)(H.Z,{checked:i.value,onChange:function(a){i.onChange(a)}})};return(0,e.jsx)(Z.gN,(0,W.Z)({component:[r]},n))},tt=t(77883),Ue=t(85482),nt=t(43358),_=t(97268),Ve=function(n){var r=n.value,u=r===void 0?{limitNum:1,limitFreq:"only"}:r,i=n.onChange;return(0,e.jsxs)("div",{children:[(0,e.jsx)(_.Z,{style:{width:100},value:u.limitFreq,onChange:function(a){return i((0,W.Z)((0,W.Z)({},u),{},{limitFreq:a}))},options:[{label:"\u53EA\u80FD",value:"only"},{label:"\u6BCF\u5C0F\u65F6",value:"hour"},{label:"\u6BCF\u5929",value:"day"},{label:"\u6BCF\u81EA\u7136\u5468",value:"week"},{label:"\u6BCF\u81EA\u7136\u6708",value:"month"},{label:"\u6BCF\u5B63\u5EA6",value:"quarter"},{label:"\u6BCF\u81EA\u7136\u5E74",value:"year"}]}),(0,e.jsx)("span",{style:{margin:"0 10px"},children:"\u7B54\u9898"}),(0,e.jsx)(Ue.Z,{min:1,max:9999,defaultValue:1,value:u.limitNum,onChange:function(a){return i((0,W.Z)((0,W.Z)({},u),{},{limitNum:a}))}}),(0,e.jsx)("span",{style:{margin:"0 10px"},children:"\u6B21"})]})},Ze=function(n){return(0,e.jsx)(Z.gN,(0,W.Z)({component:[Ve]},n))},me=t(25782),He=t(69816),b=function(){var n=function(u){var i=u.value,d=u.onChange;return i===1?(0,e.jsx)(G.Z,{type:"primary",icon:(0,e.jsx)(me.Z,{}),onClick:function(){return d(0)},children:"\u5F00\u59CB\u56DE\u6536"}):(0,e.jsx)(G.Z,{type:"primary",danger:!0,icon:(0,e.jsx)(He.Z,{}),onClick:function(){return d(1)},children:"\u6682\u505C\u56DE\u6536"})};return(0,e.jsx)(Z.gN,{name:"status",component:[n],basePath:""})};R.default.config({duration:1,maxCount:1});var h=(0,Z.Pi)(function(){var o,n=(0,Z.cI)(),r=(0,P.IE)(),u=(0,v.useState)(!!((o=r.setting)!==null&&o!==void 0&&o.answerSetting.initialValues)),i=(0,j.Z)(u,2),d=i[0],a=i[1];return(0,v.useEffect)(function(){n.setFieldState("answerSetting.initialValues",function(m){return m.display=d?"visible":"none"})},[d,n]),(0,e.jsx)(Z.RV,{form:n,children:(0,e.jsx)(Z.Wo,{name:"answerSetting",children:(0,e.jsx)("div",{children:(0,e.jsxs)(V.Z,{title:(0,e.jsxs)("div",{className:"setting-title",children:[(0,e.jsx)(T.Z,{style:{marginRight:10}}),"\u95EE\u5377\u663E\u793A"]}),className:"answer-setting",extra:(0,e.jsx)(b,{}),children:[(0,e.jsx)("div",{className:"setting-item",children:(0,e.jsxs)("div",{className:"setting-item-switch",children:[(0,e.jsxs)("span",{children:["\u5F00\u542F\u81EA\u52A8\u6682\u5B58",(0,e.jsx)(E.Z,{overlay:"\u52FE\u9009\u540E\uFF0C\u53EF\u4EE5\u81EA\u52A8\u4FDD\u5B58\u672C\u6B21\u672A\u63D0\u4EA4\u7684\u586B\u5199\u5185\u5BB9\uFF0C\u518D\u6B21\u6253\u5F00\u95EE\u5377\u53EF\u663E\u793A\u4E0A\u6B21\u672A\u63D0\u4EA4\u7684\u586B\u5199\u5185\u5BB9\u3002",children:(0,e.jsx)(F.Z,{className:"setting-prompt"})})]}),(0,e.jsx)(J,{name:"autoSave"})]})}),(0,e.jsx)("div",{className:"setting-item",children:(0,e.jsxs)("div",{className:"setting-item-switch",children:[(0,e.jsxs)("span",{children:["\u663E\u793A\u9898\u76EE\u5E8F\u53F7",(0,e.jsx)(E.Z,{overlay:"\u52FE\u9009\u540E\uFF0C\u95EE\u5377\u4E2D\u7684\u9898\u76EE\u4F1A\u6309\u6392\u5217\u987A\u5E8F\u4ECE1\u5F00\u59CB\u663E\u793A\u9898\u76EE\u5E8F\u53F7\u3002",children:(0,e.jsx)(F.Z,{className:"setting-prompt"})})]}),(0,e.jsx)(J,{name:"questionNumber"})]})}),(0,e.jsx)("div",{className:"setting-item",children:(0,e.jsxs)("div",{className:"setting-item-switch",children:[(0,e.jsxs)("span",{children:["\u663E\u793A\u8FDB\u5EA6\u6761",(0,e.jsx)(E.Z,{overlay:"\u52FE\u9009\u540E\uFF0C\u586B\u5199\u8005\u6ED1\u52A8\u9875\u9762\u53EF\u4EE5\u770B\u5230\u5F53\u524D\u95EE\u5377\u586B\u5199\u8FDB\u5EA6\u3002",children:(0,e.jsx)(F.Z,{className:"setting-prompt"})})]}),(0,e.jsx)(J,{name:"progressBar"})]})}),(0,e.jsxs)("div",{className:"setting-item",children:[(0,e.jsxs)("div",{className:"setting-item-switch",children:[(0,e.jsxs)("span",{children:["\u8BBE\u7F6E\u95EE\u5377\u9ED8\u8BA4\u7B54\u6848",(0,e.jsx)(E.Z,{overlay:"\u8BBE\u7F6E\u7684\u7B54\u6848\u5C06\u4F5C\u4E3A\u9ED8\u8BA4\u7B54\u6848\u5E26\u5165\u95EE\u5377",children:(0,e.jsx)(F.Z,{className:"setting-prompt"})})]}),(0,e.jsx)(H.Z,{checked:d,onChange:function(q){a(q)}})]}),(0,e.jsx)("div",{className:"setting-item-content",children:(0,e.jsx)(we,{name:"initialValues"})})]})]})})})})}),l=t(54421),s=t(38272),x=t(94233),f=t(51890),y=t(49111),p=t(19650),A=t(71153),M=t(60331),be=t(48736),se=t(27049),Fe=t(62999),ke=t(51753),oe=t(86582),Qe=t(18106),ue=t(67164),Q=t(68489),$e=t(60780),ce=t.n($e),ee=t(49101),Be=_.Z.Option,lt=(0,Z.Pi)(function(o){var n=o.onChange,r=o.orgTreeData,u=o.positions,i=(0,v.useState)(),d=(0,j.Z)(i,2),a=d[0],m=d[1],q=(0,v.useState)(),g=(0,j.Z)(q,2),I=g[0],D=g[1];return(0,e.jsxs)(e.Fragment,{children:[(0,e.jsxs)(w.Z,{gutter:10,children:[(0,e.jsx)(C.Z,{span:16,children:(0,e.jsx)(ke.Z,{onChange:function(S){return m(S)},style:{width:"100%"},dropdownStyle:{maxHeight:400,overflow:"auto"},treeData:[{title:"\u53D1\u8D77\u4EBA\u5F53\u524D\u90E8\u95E8",value:"${currentOrgId}",key:"${currentOrgId}"},{title:"\u53D1\u8D77\u4EBA\u4E0A\u7EA7\u90E8\u95E8",value:"${parentOrgId}",key:"${parentOrgId}"}].concat((0,oe.Z)(r)),placeholder:"\u8BF7\u9009\u62E9\u673A\u6784",treeDefaultExpandAll:!0,allowClear:!0,value:a})}),(0,e.jsx)(C.Z,{span:8,children:(0,e.jsx)(_.Z,{style:{width:"100%"},placeholder:"\u8BF7\u9009\u62E9\u5C97\u4F4D",allowClear:!0,value:I,onChange:function(S){return D(S)},children:u.map(function(k){return(0,e.jsx)(Be,{value:k.id,children:k.name},k.id)})})})]}),(0,e.jsx)(w.Z,{style:{marginTop:10},children:(0,e.jsx)(C.Z,{children:(0,e.jsx)(G.Z,{icon:(0,e.jsx)(ee.Z,{}),onClick:function(){(a||I)&&n("P:".concat(a||"",":").concat(I||""))},children:"\u6DFB\u52A0"})})})]})}),We=ue.Z.TabPane,Ke=_.Z.Option,it=(0,Z.Pi)(function(o){var n=o.onChange,r=o.tabs,u=r===void 0?["user","role","position"]:r,i=(0,Q.m2)(),d=(0,v.useState)(o.value||[]),a=(0,j.Z)(d,2),m=a[0],q=a[1];(0,v.useEffect)(function(){q(o.value||[])},[o.value]);var g=(0,Q.LF)(m),I=i.depts,D=i.users,k=i.roles,S=i.positions,le=(0,v.useState)(),ge=(0,j.Z)(le,2),te=ge[0],xe=ge[1];(0,v.useEffect)(function(){te&&i.loadUsers({orgId:te,pageSize:1024})},[te,i]);var pe=(0,v.useMemo)(function(){return ce()(I.map(function(c){return{value:c.id,title:c.name,key:c.id,parentId:c.parentId}}),{parentProperty:"parentId",customID:"value"})},[I]),de=function(N){var U=(0,oe.Z)(m);N.forEach(function(L){U.includes(L)||U.push(L)}),q(U),n(U)},ve=function(){for(var N=arguments.length,U=new Array(N),L=0;L<N;L++)U[L]=arguments[L];var K=(0,oe.Z)(m.filter(function(Ae){return!U.includes(Ae)}));q(K),n(K)};return(0,e.jsx)("div",{style:{width:400},children:(0,e.jsxs)(ue.Z,{defaultActiveKey:"user",children:[(0,e.jsx)(We,{tab:"\u6210\u5458",disabled:!u.includes("user"),children:(0,e.jsxs)("div",{style:{height:400,overflowY:"auto",overflowX:"hidden"},children:[(0,e.jsxs)(w.Z,{gutter:20,children:[(0,e.jsx)(C.Z,{span:16,children:(0,e.jsx)(ke.Z,{onChange:function(N){return xe(N)},style:{width:"100%"},dropdownStyle:{maxHeight:400,overflow:"auto"},treeData:pe,placeholder:"\u8BF7\u9009\u62E9\u673A\u6784",treeDefaultExpandAll:!0,allowClear:!0})}),(0,e.jsx)(C.Z,{span:8,children:(0,e.jsx)(_.Z,{style:{width:"100%"},placeholder:"\u8BF7\u9009\u62E9\u6210\u5458",allowClear:!0,showSearch:!0,mode:"multiple",onChange:function(N){return de(N)},maxTagCount:0,value:m.filter(function(c){return c.startsWith("U:")}),dropdownRender:function(N){return(0,e.jsxs)("div",{children:[N,(0,e.jsx)(se.Z,{style:{margin:"4px 0"}}),(0,e.jsx)("div",{style:{display:"flex",flexWrap:"nowrap",padding:8},children:(0,e.jsx)(G.Z,{size:"small",type:"link",onClick:function(){return de(D.map(function(L){return"U:".concat(L.id)}))},children:"\u5168\u9009"})})]})},children:D.map(function(c){return(0,e.jsx)(Ke,{value:"U:".concat(c.id),children:c.name},c.id)})})})]}),(0,e.jsx)(p.Z,{style:{marginTop:20},wrap:!0,children:m.filter(function(c){return c.startsWith("U:")}).map(function(c){return(0,e.jsx)(M.Z,{closable:!0,color:"blue",onClose:function(){return ve(c)},children:g.user[c.split(":")[1]]},c)})})]})},"user"),(0,e.jsx)(We,{tab:"\u89D2\u8272",disabled:!u.includes("role"),children:(0,e.jsxs)("div",{style:{height:400,overflowY:"auto",overflowX:"hidden"},children:[(0,e.jsx)(_.Z,{style:{width:"100%"},placeholder:"\u8BF7\u9009\u62E9\u89D2\u8272",allowClear:!0,showSearch:!0,mode:"multiple",value:m.filter(function(c){return c.startsWith("R:")}),onChange:function(N){return de(N)},maxTagCount:0,children:k.map(function(c){return(0,e.jsx)(Ke,{value:"R:".concat(c.id),children:c.name},c.id)})}),(0,e.jsx)(p.Z,{style:{marginTop:20},wrap:!0,children:m.filter(function(c){return c.startsWith("R:")}).map(function(c){return(0,e.jsx)(M.Z,{closable:!0,color:"blue",onClose:function(){return ve(c)},children:g.role[c.split(":")[1]]},c)})})]})},"role"),(0,e.jsx)(We,{tab:"\u673A\u6784\u5C97\u4F4D",disabled:!u.includes("position"),children:(0,e.jsxs)("div",{style:{height:400,overflowY:"auto",overflowX:"hidden"},children:[(0,e.jsx)(lt,{positions:S,orgTreeData:pe,onChange:function(N){return de([N])}}),(0,e.jsx)(p.Z,{style:{marginTop:20},wrap:!0,children:m.filter(function(c){return c.startsWith("P:")}).map(function(c){var N=c.split(":"),U=(0,j.Z)(N,3),L=U[1],K=U[2];return(0,e.jsxs)(M.Z,{closable:!0,color:"blue",onClose:function(){return ve(c)},children:[g.org[L],"-",g.position[K]]},L+":"+K)})})]})},"position")]})})}),at=it,rt=t(37809),st=function(n){var r=(0,v.useState)([]),u=(0,j.Z)(r,2),i=u[0],d=u[1];return(0,e.jsx)(fe.Z,{title:"\u8BBE\u7F6E\u534F\u4F5C\u7BA1\u7406\u5458",visible:!0,onCancel:n.onCancel,onOk:function(){return n.onOk(i)},children:(0,e.jsx)(at,{tabs:["user"],onChange:function(m){d(m.map(function(q){return q.split(":")[1]}))}})})},ot=function(){var n=(0,P.IE)(),r=n.id,u=(0,v.useState)([]),i=(0,j.Z)(u,2),d=i[0],a=i[1],m=(0,v.useState)(!1),q=(0,j.Z)(m,2),g=q[0],I=q[1],D=function(){return Q.hi.getProjectPartner(r).then(function(S){S&&a(S)})};return(0,v.useEffect)(function(){D()},[]),(0,e.jsxs)("div",{children:[(0,e.jsx)(V.Z,{className:"answer-setting",title:(0,e.jsxs)("div",{className:"setting-title",children:[(0,e.jsx)(rt.Z,{}),(0,e.jsxs)("div",{children:["\u534F\u4F5C\u7BA1\u7406\u5458\u5217\u8868",(0,e.jsx)(E.Z,{overlay:"\u534F\u4F5C\u8005\u53EF\u4EE5\u534F\u52A9\u521B\u5EFA\u8005\u8FDB\u884C\u7F16\u8F91\u95EE\u5377\u3001\u7BA1\u7406\u6570\u636E\u7B49\u64CD\u4F5C\u3002",children:(0,e.jsx)(F.Z,{className:"setting-prompt",style:{marginLeft:5}})})]})]}),extra:(0,e.jsx)("a",{href:"#",onClick:function(){return I(!0)},children:"\u8BBE\u7F6E\u534F\u4F5C\u7BA1\u7406\u5458"}),children:(0,e.jsx)(s.ZP,{itemLayout:"horizontal",dataSource:d,renderItem:function(S){return(0,e.jsxs)(s.ZP.Item,{actions:S.type!==1?[(0,e.jsx)("a",{onClick:function(){fe.Z.confirm({title:"\u5220\u9664\u534F\u4F5C\u8005",content:(0,e.jsxs)("div",{children:["\u786E\u5B9A\u5220\u9664\u534F\u4F5C\u8005 ",(0,e.jsx)("b",{children:S.user.name}),"\u5417\uFF1F"]}),onOk:function(){Q.hi.deleteProjectPartner(S.id,r).then(function(te){te.success&&D()})}})},children:"\u5220\u9664"},"list-loadmore-edit")]:[],children:[(0,e.jsx)(s.ZP.Item.Meta,{avatar:(0,e.jsx)(f.C,{src:S.user.avatar?"/api/public/preview/".concat(S.user.avatar):""}),title:S.user.name}),(0,e.jsx)("div",{children:S.type===1?"\u521B\u5EFA\u8005":"\u534F\u4F5C\u8005"})]})}})}),g&&(0,e.jsx)(st,{onCancel:function(){return I(!1)},onOk:function(S){Q.hi.addProjectPartner({userIds:S,projectId:r}).then(function(){D(),I(!1)})}})]})},ut=t(56931),Pe=t(64031),ct=(0,Z.Pi)(function(){var o,n,r=(0,P.IE)(),u=(0,v.useMemo)(function(){return(0,Pe.Np)({initialValues:r.setting,effects:function(){(0,Pe.Zj)("*",function(g){console.log(g.address.toString(),g.value);var I={},D=I;g.path.forEach(function(k,S){var le={};S===g.path.length-1?D[k]=g.value:D[k]=le,D=le}),Q.hi.updateSetting({id:r.id,settingKey:g.address.toString(),settingValue:g.value}).then(function(k){k.success&&R.default.info("\u4FDD\u5B58\u4E2D")})})}})},[r.setting]),i=(0,v.useState)(!!((o=r.setting)!==null&&o!==void 0&&(n=o.submittedSetting)!==null&&n!==void 0&&n.contentHtml)),d=(0,j.Z)(i,2),a=d[0],m=d[1];return(0,v.useEffect)(function(){u.setFieldState("submittedSetting.contentHtml",function(q){return q.display=a?"visible":"none"})},[a,u]),(0,e.jsx)(Z.RV,{form:u,children:(0,e.jsx)(Z.Wo,{name:"submittedSetting",children:(0,e.jsx)("div",{children:(0,e.jsx)(V.Z,{title:(0,e.jsxs)("div",{className:"setting-title",children:[(0,e.jsx)(ut.Z,{}),"\u586B\u5199\u8005\u63D0\u4EA4\u95EE\u5377\u540E"]}),className:"submitted-setting",children:(0,e.jsxs)("div",{className:"setting-item",children:[(0,e.jsxs)("div",{className:"setting-item-switch",children:[(0,e.jsxs)("span",{children:["\u7B54\u9898\u5B8C\u6210\u540E\u8DF3\u8F6C\u81EA\u5B9A\u4E49\u9875\u9762",(0,e.jsx)(E.Z,{overlay:"\u4F60\u53EF\u4EE5\u5728\u8868\u5355\u63D0\u4EA4\u9875\u9762\u8BBE\u7F6E\u66F4\u4E3A\u4E30\u5BCC\u591A\u5F69\u7684\u5185\u5BB9\uFF0C\u5305\u62EC\u63D2\u5165\u56FE\u7247\u3001\u8BBE\u7F6E\u5B57\u53F7\u3001\u5B57\u4F53\u989C\u8272\u3001\u5E8F\u53F7\u3001\u8BBE\u7F6E\u8D85\u94FE\u63A5\u7B49\u7B49\u3002",children:(0,e.jsx)(F.Z,{className:"setting-prompt"})})]}),(0,e.jsx)(H.Z,{checked:a,onChange:function(g){m(g)}})]}),(0,e.jsx)("div",{className:"setting-item-content",children:(0,e.jsx)(Se,{name:"contentHtml"})})]})})})})})}),yt=t(14965),dt=t(77121),Ye=t(1603),Xe=t(90586);R.default.config({duration:1,maxCount:1});var vt=(0,Z.Pi)(function(){var o,n,r,u,i,d=(0,P.IE)(),a=(0,Z.cI)(),m=(0,v.useState)(!!((o=d.setting)!==null&&o!==void 0&&o.answerSetting.cookieLimit)),q=(0,j.Z)(m,2),g=q[0],I=q[1],D=(0,v.useState)(!!((n=d.setting)!==null&&n!==void 0&&n.answerSetting.ipLimit)),k=(0,j.Z)(D,2),S=k[0],le=k[1],ge=(0,v.useState)(!!((r=d.setting)!==null&&r!==void 0&&r.answerSetting.password)),te=(0,j.Z)(ge,2),xe=te[0],pe=te[1];(0,v.useEffect)(function(){var O;pe(!!((O=d.setting)!==null&&O!==void 0&&O.answerSetting.password))},[d.setting]),(0,v.useEffect)(function(){a.setFieldState("answerSetting.password",function(O){return O.display=xe?"visible":"none"})},[xe,a]),(0,v.useEffect)(function(){a.setFieldState("answerSetting.cookieLimit",function(B){return B.display=g?"visible":"none"});var O=a.getFieldState("answerSetting.cookieLimit",function(B){return B.value});g&&!O&&a.setFieldState("answerSetting.cookieLimit",function(B){return B.value={limitNum:1,limitFreq:"only"}})},[g,a]),(0,v.useEffect)(function(){a.setFieldState("answerSetting.ipLimit",function(B){return B.display=S?"visible":"none"});var O=a.getFieldState("answerSetting.ipLimit",function(B){return B.value});S&&!O&&a.setFieldState("answerSetting.ipLimit",function(B){return B.value={limitNum:1,limitFreq:"only"}})},[S,a]);var de=(0,v.useState)(!!((u=d.setting)!==null&&u!==void 0&&u.answerSetting.endTime)),ve=(0,j.Z)(de,2),c=ve[0],N=ve[1],U=(0,v.useState)(!!((i=d.setting)!==null&&i!==void 0&&i.answerSetting.maxAnswers)),L=(0,j.Z)(U,2),K=L[0],Ae=L[1];return(0,v.useEffect)(function(){a.setFieldState("answerSetting.endTime",function(O){return O.display=c?"visible":"none"})},[c,a]),(0,v.useEffect)(function(){a.setFieldState("answerSetting.maxAnswers",function(O){return O.display=K?"visible":"none"})},[K,a]),(0,e.jsx)("div",{children:(0,e.jsxs)(V.Z,{title:(0,e.jsxs)("div",{className:"setting-title",children:[(0,e.jsx)(Ye.Z,{style:{marginRight:10}}),"\u56DE\u6536\u8BBE\u7F6E"]}),className:"answer-setting",children:[(0,e.jsx)("div",{className:"setting-item",children:(0,e.jsxs)("div",{className:"setting-item-switch",children:[(0,e.jsxs)("span",{children:["\u9700\u8981\u767B\u5F55",(0,e.jsx)(E.Z,{overlay:"\u52FE\u9009\u540E\uFF0C\u53EA\u6709\u767B\u5F55\u7528\u6237\u624D\u53EF\u4EE5\u586B\u5199\u95EE\u5377\u3002",children:(0,e.jsx)(F.Z,{className:"setting-prompt"})})]}),(0,e.jsx)(J,{name:"loginRequired",basePath:"answerSetting"})]})}),(0,e.jsx)("div",{className:"setting-item",children:(0,e.jsxs)("div",{className:"setting-item-switch",children:[(0,e.jsx)("span",{children:"\u53EA\u80FD\u5FAE\u4FE1\u586B\u5199"}),(0,e.jsx)(J,{name:"wechatOnly",basePath:"answerSetting"})]})}),(0,e.jsxs)("div",{className:"setting-item",children:[(0,e.jsxs)("div",{className:"setting-item-switch",children:[(0,e.jsxs)("span",{children:["\u51ED\u5BC6\u7801\u586B\u5199",(0,e.jsx)(E.Z,{overlay:"\u53EA\u6709\u8F93\u5165\u5BC6\u7801\u624D\u80FD\u586B\u5199\u95EE\u5377",children:(0,e.jsx)(F.Z,{className:"setting-prompt"})})]}),(0,e.jsx)(H.Z,{checked:xe,onChange:function(B){pe(B)}})]}),(0,e.jsx)("div",{className:"setting-item-content",children:(0,e.jsx)(Te,{style:{width:200},name:"password",basePath:"answerSetting"})})]}),(0,e.jsxs)("div",{className:"setting-item",children:[(0,e.jsxs)("div",{className:"setting-item-switch",children:[(0,e.jsxs)("span",{children:["\u6BCF\u53F0\u7535\u8111\u6216\u624B\u673A\u7B54\u9898\u6B21\u6570\u9650\u5236",(0,e.jsx)(E.Z,{overlay:"\u6839\u636E cookie \u8FDB\u884C\u9650\u5236",children:(0,e.jsx)(F.Z,{className:"setting-prompt"})})]}),(0,e.jsx)(H.Z,{checked:g,onChange:function(B){I(B)}})]}),(0,e.jsx)("div",{className:"setting-item-content",children:(0,e.jsx)(Ze,{name:"cookieLimit",basePath:"answerSetting"})})]}),(0,e.jsxs)("div",{className:"setting-item",children:[(0,e.jsxs)("div",{className:"setting-item-switch",children:[(0,e.jsxs)("span",{children:["\u6BCF\u4E2AIP\u7B54\u9898\u6B21\u6570\u9650\u5236",(0,e.jsx)(E.Z,{overlay:"\u6839\u636E IP \u8FDB\u884C\u9650\u5236",children:(0,e.jsx)(F.Z,{className:"setting-prompt"})})]}),(0,e.jsx)(H.Z,{checked:S,onChange:function(B){le(B)}})]}),(0,e.jsx)("div",{className:"setting-item-content",children:(0,e.jsx)(Ze,{name:"ipLimit",basePath:"answerSetting"})})]}),(0,e.jsxs)("div",{className:"setting-item",children:[(0,e.jsxs)("div",{className:"setting-item-switch",children:[(0,e.jsx)("span",{children:"\u8BBE\u7F6E\u95EE\u5377\u7ED3\u675F\u65F6\u95F4"}),(0,e.jsx)(H.Z,{checked:c,onChange:function(B){N(B)}})]}),(0,e.jsx)("div",{className:"setting-item-content",children:(0,e.jsx)(Xe.P,{name:"endTime",isMoment:!0,basePath:"answerSetting",children:(0,e.jsx)(dt.Z,{showTime:!0})})})]}),(0,e.jsxs)("div",{className:"setting-item",children:[(0,e.jsxs)("div",{className:"setting-item-switch",children:[(0,e.jsx)("span",{children:"\u8BBE\u7F6E\u95EE\u5377\u56DE\u6536\u4E0A\u9650"}),(0,e.jsx)(H.Z,{checked:K,onChange:function(B){Ae(B)}})]}),(0,e.jsx)("div",{className:"setting-item-content",children:K&&(0,e.jsxs)(e.Fragment,{children:["\u56DE\u6536",(0,e.jsx)(Xe.P,{name:"maxAnswers",basePath:"answerSetting",children:(0,e.jsx)(Ue.Z,{style:{margin:"0 5px"},min:1})}),"\u4EFD\u95EE\u5377\u540E\uFF0C\u505C\u6B62\u6536\u96C6"]})})]})]})})}),ht=(0,Z.Pi)(function(){var o=X("-content"),n=(0,P.IE)(),r=(0,v.useMemo)(function(){return(0,Pe.Np)({initialValues:n.setting,effects:function(){(0,Pe.Zj)("*",function(i){console.log(i.address.toString(),i.value);var d={},a=d;i.path.forEach(function(m,q){var g={};q===i.path.length-1?a[m]=i.value:a[m]=g,a=g}),Q.hi.updateSetting({id:n.id,settingKey:i.address.toString(),settingValue:i.value}).then(function(m){m.success&&R.default.info("\u4FDD\u5B58\u4E2D")})})}})},[n.setting]);return(0,e.jsx)(Z.RV,{form:r,children:(0,e.jsx)("div",{className:o,children:(0,e.jsxs)(w.Z,{gutter:[2,2],children:[(0,e.jsx)(C.Z,{xs:24,sm:8,xl:6,children:(0,e.jsx)(h,{})}),(0,e.jsx)(C.Z,{xs:24,sm:8,xl:6,children:(0,e.jsx)(vt,{})}),(0,e.jsx)(C.Z,{xs:24,sm:8,xl:6,children:(0,e.jsx)(ct,{})}),(0,e.jsx)(C.Z,{xs:24,sm:8,xl:6,children:(0,e.jsx)(ot,{})})]})})})}),St=t(7359),Ge=t(27279),Je=t(8212),ft=t(50206),mt=t(94184),bt=t.n(mt),_e=Ge.Z.Panel,gt=[{key:"answerSetting",title:"\u6570\u636E\u6536\u96C6\u8BBE\u7F6E",icon:(0,e.jsx)(Je.Z,{}),description:"\u57FA\u672C\u7684\u95EE\u5377\u586B\u5199\u8BBE\u7F6E"},{key:"answerLimitSetting",title:"\u7B54\u9898\u9650\u5236",icon:(0,e.jsx)(Je.Z,{}),description:"\u95EE\u5377\u7B54\u9898\u9650\u5236"},{key:"memberSetting",title:"\u534F\u540C\u7F16\u8F91",icon:(0,e.jsx)(ft.Z,{}),description:"\u8BBE\u7F6E\u534F\u4F5C\u7BA1\u7406\u5458"},{key:"submittedSetting",title:"\u63D0\u4EA4\u95EE\u5377\u6570\u636E\u540E",icon:(0,e.jsx)(Ye.Z,{}),description:"\u95EE\u5377\u63D0\u4EA4\u5B8C\u6210\u9875\u9762\u8BBE\u7F6E"}],Zt=(0,Z.Pi)(function(){var o=X("-nav"),n=(0,v.useContext)(ne),r=n.settingStore,u=r.activePanel;return(0,e.jsx)("div",{className:o,children:(0,e.jsxs)(Ge.Z,{accordion:!0,expandIcon:function(d){var a=d.isActive;return(0,e.jsx)(me.Z,{rotate:a?90:0})},defaultActiveKey:"1",children:[(0,e.jsx)(_e,{header:"\u57FA\u7840\u8BBE\u7F6E",style:{padding:0},children:gt.map(function(i){return(0,e.jsxs)(w.Z,{className:bt()("nav-panel-item",{active:u===i.key}),onClick:function(){return r.activePanel=i.key},children:[(0,e.jsx)(C.Z,{span:4,children:(0,e.jsx)(f.C,{icon:i.icon,shape:"square"})}),(0,e.jsx)(C.Z,{span:20,children:(0,e.jsxs)(w.Z,{children:[(0,e.jsx)(C.Z,{span:24,children:i.title}),(0,e.jsx)(C.Z,{span:24,style:{fontSize:12,color:"#8c8c8c"},children:i.description})]})})]},i.key)})},"1"),(0,e.jsx)(_e,{header:"\u9AD8\u7EA7\u8BBE\u7F6E",children:(0,e.jsx)("p",{children:"todo"})},"2")]})})}),xt=t(69610),pt=t(54941),qt=function(){function o(n){(0,xt.Z)(this,o),this.rootStore=void 0,this.activePanel=void 0,this.rootStore=n,this.activePanel="answerSetting",this.makeObservable()}return(0,pt.Z)(o,[{key:"makeObservable",value:function(){(0,re.Ou)(this,{rootStore:re.LO.ref,activePanel:re.LO.ref})}}]),o}(),jt=t(77613),Ct=t(27400),et=(0,jt.P)(function(){var o=(0,P.IE)(),n=(0,Ct.a)(),r=n.isMobile,u=(0,v.useMemo)(function(){return new qt(o)},[o]);return(0,e.jsx)(ne.Provider,{value:{prefixCls:"survey-setting",settingStore:u},children:(0,e.jsx)("div",{className:"survey-setting",children:(0,e.jsx)(ht,{})})})}),Et=et},11628:function(De,Y,t){"use strict";t.d(Y,{xI:function(){return w},Ge:function(){return ie},IE:function(){return C}});var $=t(67294),w=(0,$.createContext)({}),z=w.Provider;function C(){var R=(0,$.useContext)(w);return R.store}function ie(){var R=C();return R.flowStore}}}]);
