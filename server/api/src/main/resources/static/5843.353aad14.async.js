(self.webpackChunksurvey_king=self.webpackChunksurvey_king||[]).push([[5843],{1870:function(Ye,xe,u){"use strict";u.d(xe,{Z:function(){return ve}});var V=u(28991),M=u(67294),b={icon:{tag:"svg",attrs:{viewBox:"64 64 896 896",focusable:"false"},children:[{tag:"path",attrs:{d:"M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z"}},{tag:"path",attrs:{d:"M623.6 316.7C593.6 290.4 554 276 512 276s-81.6 14.5-111.6 40.7C369.2 344 352 380.7 352 420v7.6c0 4.4 3.6 8 8 8h48c4.4 0 8-3.6 8-8V420c0-44.1 43.1-80 96-80s96 35.9 96 80c0 31.1-22 59.6-56.1 72.7-21.2 8.1-39.2 22.3-52.1 40.9-13.1 19-19.9 41.8-19.9 64.9V620c0 4.4 3.6 8 8 8h48c4.4 0 8-3.6 8-8v-22.7a48.3 48.3 0 0130.9-44.8c59-22.7 97.1-74.7 97.1-132.5.1-39.3-17.1-76-48.3-103.3zM472 732a40 40 0 1080 0 40 40 0 10-80 0z"}}]},name:"question-circle",theme:"outlined"},q=b,Ze=u(27029),X=function(ce,ge){return M.createElement(Ze.Z,(0,V.Z)((0,V.Z)({},ce),{},{ref:ge,icon:q}))};X.displayName="QuestionCircleOutlined";var ve=M.forwardRef(X)},55843:function(Ye,xe,u){"use strict";u.d(xe,{Z:function(){return kt}});var V=u(65223),M=u(96156),b=u(22122),q=u(85061),Ze=u(94184),X=u.n(Ze),ve=u(5461),n=u(67294),ce=u(53124),ge=u(33603),J=u(28481);function he(e){var t=n.useState(e),a=(0,J.Z)(t,2),r=a[0],l=a[1];return n.useEffect(function(){var i=setTimeout(function(){l(e)},e.length?0:10);return function(){clearTimeout(i)}},[e]),r}var Re=[];function Ee(e,t,a){var r=arguments.length>3&&arguments[3]!==void 0?arguments[3]:0;return{key:typeof e=="string"?e:"".concat(a,"-").concat(r),error:e,errorStatus:t}}function we(e){var t=e.help,a=e.helpStatus,r=e.errors,l=r===void 0?Re:r,i=e.warnings,s=i===void 0?Re:i,o=e.className,m=e.fieldId,d=e.onVisibleChanged,Z=n.useContext(V.Rk),h=Z.prefixCls,I=n.useContext(ce.E_),y=I.getPrefixCls,C="".concat(h,"-item-explain"),E=y(),T=he(l),g=he(s),p=n.useMemo(function(){return t!=null?[Ee(t,a,"help")]:[].concat((0,q.Z)(T.map(function(F,x){return Ee(F,"error","error",x)})),(0,q.Z)(g.map(function(F,x){return Ee(F,"warning","warning",x)})))},[t,a,T,g]),N={};return m&&(N.id="".concat(m,"_help")),n.createElement(ve.default,{motionDeadline:ge.ZP.motionDeadline,motionName:"".concat(E,"-show-help"),visible:!!p.length,onVisibleChanged:d},function(F){var x=F.className,P=F.style;return n.createElement("div",(0,b.Z)({},N,{className:X()(C,x,o),style:P,role:"alert"}),n.createElement(ve.CSSMotionList,(0,b.Z)({keys:p},ge.ZP,{motionName:"".concat(E,"-show-help-item"),component:!1}),function(R){var S=R.key,w=R.error,f=R.errorStatus,j=R.className,$=R.style;return n.createElement("div",{key:S,className:X()(j,(0,M.Z)({},"".concat(C,"-").concat(f),f)),style:$},w)}))})}var Ce=u(90484),se=u(48526),Oe=u(98866),Me=u(97647);function Le(e){return typeof e=="object"&&e!=null&&e.nodeType===1}function Pe(e,t){return(!t||e!=="hidden")&&e!=="visible"&&e!=="clip"}function Fe(e,t){if(e.clientHeight<e.scrollHeight||e.clientWidth<e.scrollWidth){var a=getComputedStyle(e,null);return Pe(a.overflowY,t)||Pe(a.overflowX,t)||function(r){var l=function(i){if(!i.ownerDocument||!i.ownerDocument.defaultView)return null;try{return i.ownerDocument.defaultView.frameElement}catch(s){return null}}(r);return!!l&&(l.clientHeight<r.scrollHeight||l.clientWidth<r.scrollWidth)}(e)}return!1}function be(e,t,a,r,l,i,s,o){return i<e&&s>t||i>e&&s<t?0:i<=e&&o<=a||s>=t&&o>=a?i-e-r:s>t&&o<a||i<e&&o>a?s-t+l:0}var Ve=function(e,t){var a=window,r=t.scrollMode,l=t.block,i=t.inline,s=t.boundary,o=t.skipOverflowHiddenElements,m=typeof s=="function"?s:function(oe){return oe!==s};if(!Le(e))throw new TypeError("Invalid target");for(var d,Z,h=document.scrollingElement||document.documentElement,I=[],y=e;Le(y)&&m(y);){if((y=(Z=(d=y).parentElement)==null?d.getRootNode().host||null:Z)===h){I.push(y);break}y!=null&&y===document.body&&Fe(y)&&!Fe(document.documentElement)||y!=null&&Fe(y,o)&&I.push(y)}for(var C=a.visualViewport?a.visualViewport.width:innerWidth,E=a.visualViewport?a.visualViewport.height:innerHeight,T=window.scrollX||pageXOffset,g=window.scrollY||pageYOffset,p=e.getBoundingClientRect(),N=p.height,F=p.width,x=p.top,P=p.right,R=p.bottom,S=p.left,w=l==="start"||l==="nearest"?x:l==="end"?R:x+N/2,f=i==="center"?S+F/2:i==="end"?P:S,j=[],$=0;$<I.length;$++){var c=I[$],v=c.getBoundingClientRect(),z=v.height,Q=v.width,L=v.top,W=v.right,ue=v.bottom,le=v.left;if(r==="if-needed"&&x>=0&&S>=0&&R<=E&&P<=C&&x>=L&&R<=ue&&S>=le&&P<=W)return j;var D=getComputedStyle(c),re=parseInt(D.borderLeftWidth,10),ne=parseInt(D.borderTopWidth,10),B=parseInt(D.borderRightWidth,10),ie=parseInt(D.borderBottomWidth,10),K=0,U=0,_="offsetWidth"in c?c.offsetWidth-c.clientWidth-re-B:0,A="offsetHeight"in c?c.offsetHeight-c.clientHeight-ne-ie:0,O="offsetWidth"in c?c.offsetWidth===0?0:Q/c.offsetWidth:0,k="offsetHeight"in c?c.offsetHeight===0?0:z/c.offsetHeight:0;if(h===c)K=l==="start"?w:l==="end"?w-E:l==="nearest"?be(g,g+E,E,ne,ie,g+w,g+w+N,N):w-E/2,U=i==="start"?f:i==="center"?f-C/2:i==="end"?f-C:be(T,T+C,C,re,B,T+f,T+f+F,F),K=Math.max(0,K+g),U=Math.max(0,U+T);else{K=l==="start"?w-L-ne:l==="end"?w-ue+ie+A:l==="nearest"?be(L,ue,z,ne,ie+A,w,w+N,N):w-(L+z/2)+A/2,U=i==="start"?f-le-re:i==="center"?f-(le+Q/2)+_/2:i==="end"?f-W+B+_:be(le,W,Q,re,B+_,f,f+F,F);var Y=c.scrollLeft,H=c.scrollTop;w+=H-(K=Math.max(0,Math.min(H+K/k,c.scrollHeight-z/k+A))),f+=Y-(U=Math.max(0,Math.min(Y+U/O,c.scrollWidth-Q/O+_)))}j.push({el:c,top:K,left:U})}return j};function Te(e){return e===Object(e)&&Object.keys(e).length!==0}function Xe(e,t){t===void 0&&(t="auto");var a="scrollBehavior"in document.body.style;e.forEach(function(r){var l=r.el,i=r.top,s=r.left;l.scroll&&a?l.scroll({top:i,left:s,behavior:t}):(l.scrollTop=i,l.scrollLeft=s)})}function Be(e){return e===!1?{block:"end",inline:"nearest"}:Te(e)?e:{block:"start",inline:"nearest"}}function Ge(e,t){var a=e.isConnected||e.ownerDocument.documentElement.contains(e);if(Te(t)&&typeof t.behavior=="function")return t.behavior(a?Ve(e,t):[]);if(!!a){var r=Be(t);return Xe(Ve(e,r),r.behavior)}}var Je=Ge,_e=["parentNode"],et="form_item";function fe(e){return e===void 0||e===!1?[]:Array.isArray(e)?e:[e]}function je(e,t){if(!!e.length){var a=e.join("_");if(t)return"".concat(t,"_").concat(a);var r=_e.indexOf(a)>=0;return r?"".concat(et,"_").concat(a):a}}function We(e){var t=fe(e);return t.join("_")}function $e(e){var t=(0,se.useForm)(),a=(0,J.Z)(t,1),r=a[0],l=n.useRef({}),i=n.useMemo(function(){return e!=null?e:(0,b.Z)((0,b.Z)({},r),{__INTERNAL__:{itemRef:function(o){return function(m){var d=We(o);m?l.current[d]=m:delete l.current[d]}}},scrollToField:function(o){var m=arguments.length>1&&arguments[1]!==void 0?arguments[1]:{},d=fe(o),Z=je(d,i.__INTERNAL__.name),h=Z?document.getElementById(Z):null;h&&Je(h,(0,b.Z)({scrollMode:"if-needed",block:"nearest"},m))},getFieldInstance:function(o){var m=We(o);return l.current[m]}})},[e,r]);return[i]}var tt=function(e,t){var a={};for(var r in e)Object.prototype.hasOwnProperty.call(e,r)&&t.indexOf(r)<0&&(a[r]=e[r]);if(e!=null&&typeof Object.getOwnPropertySymbols=="function")for(var l=0,r=Object.getOwnPropertySymbols(e);l<r.length;l++)t.indexOf(r[l])<0&&Object.prototype.propertyIsEnumerable.call(e,r[l])&&(a[r[l]]=e[r[l]]);return a},rt=function(t,a){var r,l=n.useContext(Me.Z),i=n.useContext(Oe.Z),s=n.useContext(ce.E_),o=s.getPrefixCls,m=s.direction,d=s.form,Z=t.prefixCls,h=t.className,I=h===void 0?"":h,y=t.size,C=y===void 0?l:y,E=t.disabled,T=E===void 0?i:E,g=t.form,p=t.colon,N=t.labelAlign,F=t.labelWrap,x=t.labelCol,P=t.wrapperCol,R=t.hideRequiredMark,S=t.layout,w=S===void 0?"horizontal":S,f=t.scrollToFirstError,j=t.requiredMark,$=t.onFinishFailed,c=t.name,v=tt(t,["prefixCls","className","size","disabled","form","colon","labelAlign","labelWrap","labelCol","wrapperCol","hideRequiredMark","layout","scrollToFirstError","requiredMark","onFinishFailed","name"]),z=(0,n.useMemo)(function(){return j!==void 0?j:d&&d.requiredMark!==void 0?d.requiredMark:!R},[R,j,d]),Q=p!=null?p:d==null?void 0:d.colon,L=o("form",Z),W=X()(L,(r={},(0,M.Z)(r,"".concat(L,"-").concat(w),!0),(0,M.Z)(r,"".concat(L,"-hide-required-mark"),z===!1),(0,M.Z)(r,"".concat(L,"-rtl"),m==="rtl"),(0,M.Z)(r,"".concat(L,"-").concat(C),C),r),I),ue=$e(g),le=(0,J.Z)(ue,1),D=le[0],re=D.__INTERNAL__;re.name=c;var ne=(0,n.useMemo)(function(){return{name:c,labelAlign:N,labelCol:x,labelWrap:F,wrapperCol:P,vertical:w==="vertical",colon:Q,requiredMark:z,itemRef:re.itemRef,form:D}},[c,N,x,P,w,Q,z,D]);n.useImperativeHandle(a,function(){return D});var B=function(K){$==null||$(K);var U={block:"nearest"};f&&K.errorFields.length&&((0,Ce.Z)(f)==="object"&&(U=f),D.scrollToField(K.errorFields[0].name,U))};return n.createElement(Oe.n,{disabled:T},n.createElement(Me.q,{size:C},n.createElement(V.q3.Provider,{value:ne},n.createElement(se.default,(0,b.Z)({id:c},v,{name:c,onFinishFailed:B,form:D,className:W})))))},nt=n.forwardRef(rt),at=nt,lt=u(30470),Ae=u(42550),it=function(){var t=(0,n.useContext)(V.aM),a=t.status;return{status:a}},ot=it,ke=u(96159),st=u(93355),ze=u(75164);function ut(e){var t=n.useState(e),a=(0,J.Z)(t,2),r=a[0],l=a[1],i=(0,n.useRef)(null),s=(0,n.useRef)([]),o=(0,n.useRef)(!1);n.useEffect(function(){return o.current=!1,function(){o.current=!0,ze.Z.cancel(i.current),i.current=null}},[]);function m(d){o.current||(i.current===null&&(s.current=[],i.current=(0,ze.Z)(function(){i.current=null,l(function(Z){var h=Z;return s.current.forEach(function(I){h=I(h)}),h})})),s.current.push(d))}return[r,m]}function ct(){var e=n.useContext(V.q3),t=e.itemRef,a=n.useRef({});function r(l,i){var s=i&&(0,Ce.Z)(i)==="object"&&i.ref,o=l.join("_");return(a.current.name!==o||a.current.originRef!==s)&&(a.current.name=o,a.current.originRef=s,a.current.ref=(0,Ae.sQ)(t(l),s)),a.current.ref}return r}var dt=u(38819),ft=u(43061),mt=u(68855),vt=u(7085),gt=u(8410),ht=u(98423),Ct=u(92820),bt=u(1870),He=u(21584),yt=u(42051),xt=u(85636),Zt=u(94199),Et=function(e,t){var a={};for(var r in e)Object.prototype.hasOwnProperty.call(e,r)&&t.indexOf(r)<0&&(a[r]=e[r]);if(e!=null&&typeof Object.getOwnPropertySymbols=="function")for(var l=0,r=Object.getOwnPropertySymbols(e);l<r.length;l++)t.indexOf(r[l])<0&&Object.prototype.propertyIsEnumerable.call(e,r[l])&&(a[r[l]]=e[r[l]]);return a};function Ft(e){return e?(0,Ce.Z)(e)==="object"&&!n.isValidElement(e)?e:{title:e}:null}var It=function(t){var a=t.prefixCls,r=t.label,l=t.htmlFor,i=t.labelCol,s=t.labelAlign,o=t.colon,m=t.required,d=t.requiredMark,Z=t.tooltip,h=(0,yt.E)("Form"),I=(0,J.Z)(h,1),y=I[0];return r?n.createElement(V.q3.Consumer,{key:"label"},function(C){var E,T=C.vertical,g=C.labelAlign,p=C.labelCol,N=C.labelWrap,F=C.colon,x,P=i||p||{},R=s||g,S="".concat(a,"-item-label"),w=X()(S,R==="left"&&"".concat(S,"-left"),P.className,(0,M.Z)({},"".concat(S,"-wrap"),!!N)),f=r,j=o===!0||F!==!1&&o!==!1,$=j&&!T;$&&typeof r=="string"&&r.trim()!==""&&(f=r.replace(/[:|：]\s*$/,""));var c=Ft(Z);if(c){var v=c.icon,z=v===void 0?n.createElement(bt.Z,null):v,Q=Et(c,["icon"]),L=n.createElement(Zt.Z,(0,b.Z)({},Q),n.cloneElement(z,{className:"".concat(a,"-item-tooltip"),title:""}));f=n.createElement(n.Fragment,null,f,L)}d==="optional"&&!m&&(f=n.createElement(n.Fragment,null,f,n.createElement("span",{className:"".concat(a,"-item-optional"),title:""},(y==null?void 0:y.optional)||((x=xt.Z.Form)===null||x===void 0?void 0:x.optional))));var W=X()((E={},(0,M.Z)(E,"".concat(a,"-item-required"),m),(0,M.Z)(E,"".concat(a,"-item-required-mark-optional"),d==="optional"),(0,M.Z)(E,"".concat(a,"-item-no-colon"),!j),E));return n.createElement(He.Z,(0,b.Z)({},P,{className:w}),n.createElement("label",{htmlFor:l,className:W,title:typeof r=="string"?r:""},f))}):null},St=It,pt=function(t){var a=t.prefixCls,r=t.status,l=t.wrapperCol,i=t.children,s=t.errors,o=t.warnings,m=t._internalItemRender,d=t.extra,Z=t.help,h=t.fieldId,I=t.marginBottom,y=t.onErrorVisibleChanged,C="".concat(a,"-item"),E=n.useContext(V.q3),T=l||E.wrapperCol||{},g=X()("".concat(C,"-control"),T.className),p=n.useMemo(function(){return(0,b.Z)({},E)},[E]);delete p.labelCol,delete p.wrapperCol;var N=n.createElement("div",{className:"".concat(C,"-control-input")},n.createElement("div",{className:"".concat(C,"-control-input-content")},i)),F=n.useMemo(function(){return{prefixCls:a,status:r}},[a,r]),x=I!==null||s.length||o.length?n.createElement("div",{style:{display:"flex",flexWrap:"nowrap"}},n.createElement(V.Rk.Provider,{value:F},n.createElement(we,{fieldId:h,errors:s,warnings:o,help:Z,helpStatus:r,className:"".concat(C,"-explain-connected"),onVisibleChanged:y})),!!I&&n.createElement("div",{style:{width:0,height:I}})):null,P={};h&&(P.id="".concat(h,"_extra"));var R=d?n.createElement("div",(0,b.Z)({},P,{className:"".concat(C,"-extra")}),d):null,S=m&&m.mark==="pro_table_render"&&m.render?m.render(t,{input:N,errorList:x,extra:R}):n.createElement(n.Fragment,null,N,x,R);return n.createElement(V.q3.Provider,{value:p},n.createElement(He.Z,(0,b.Z)({},T,{className:g}),S))},Nt=pt,Rt=function(e,t){var a={};for(var r in e)Object.prototype.hasOwnProperty.call(e,r)&&t.indexOf(r)<0&&(a[r]=e[r]);if(e!=null&&typeof Object.getOwnPropertySymbols=="function")for(var l=0,r=Object.getOwnPropertySymbols(e);l<r.length;l++)t.indexOf(r[l])<0&&Object.prototype.propertyIsEnumerable.call(e,r[l])&&(a[r[l]]=e[r[l]]);return a},wt={success:dt.Z,warning:mt.Z,error:ft.Z,validating:vt.Z};function Ot(e){var t,a=e.prefixCls,r=e.className,l=e.style,i=e.help,s=e.errors,o=e.warnings,m=e.validateStatus,d=e.meta,Z=e.hasFeedback,h=e.hidden,I=e.children,y=e.fieldId,C=e.isRequired,E=e.onSubItemMetaChange,T=Rt(e,["prefixCls","className","style","help","errors","warnings","validateStatus","meta","hasFeedback","hidden","children","fieldId","isRequired","onSubItemMetaChange"]),g="".concat(a,"-item"),p=n.useContext(V.q3),N=p.requiredMark,F=n.useRef(null),x=he(s),P=he(o),R=i!=null,S=!!(R||s.length||o.length),w=n.useState(null),f=(0,J.Z)(w,2),j=f[0],$=f[1];(0,gt.Z)(function(){if(S&&F.current){var L=getComputedStyle(F.current);$(parseInt(L.marginBottom,10))}},[S]);var c=function(W){W||$(null)},v="";m!==void 0?v=m:d.validating?v="validating":x.length?v="error":P.length?v="warning":d.touched&&(v="success");var z=n.useMemo(function(){var L;if(Z){var W=v&&wt[v];L=W?n.createElement("span",{className:X()("".concat(g,"-feedback-icon"),"".concat(g,"-feedback-icon-").concat(v))},n.createElement(W,null)):null}return{status:v,hasFeedback:Z,feedbackIcon:L,isFormItemInput:!0}},[v,Z]),Q=(t={},(0,M.Z)(t,g,!0),(0,M.Z)(t,"".concat(g,"-with-help"),R||x.length||P.length),(0,M.Z)(t,"".concat(r),!!r),(0,M.Z)(t,"".concat(g,"-has-feedback"),v&&Z),(0,M.Z)(t,"".concat(g,"-has-success"),v==="success"),(0,M.Z)(t,"".concat(g,"-has-warning"),v==="warning"),(0,M.Z)(t,"".concat(g,"-has-error"),v==="error"),(0,M.Z)(t,"".concat(g,"-is-validating"),v==="validating"),(0,M.Z)(t,"".concat(g,"-hidden"),h),t);return n.createElement("div",{className:X()(Q),style:l,ref:F},n.createElement(Ct.Z,(0,b.Z)({className:"".concat(g,"-row")},(0,ht.Z)(T,["_internalItemRender","colon","dependencies","extra","fieldKey","getValueFromEvent","getValueProps","htmlFor","id","initialValue","isListField","label","labelAlign","labelCol","labelWrap","messageVariables","name","normalize","noStyle","preserve","required","requiredMark","rules","shouldUpdate","trigger","tooltip","validateFirst","validateTrigger","valuePropName","wrapperCol"])),n.createElement(St,(0,b.Z)({htmlFor:y,required:C,requiredMark:N},e,{prefixCls:a})),n.createElement(Nt,(0,b.Z)({},e,d,{errors:x,warnings:P,prefixCls:a,status:v,help:i,marginBottom:j,onErrorVisibleChanged:c}),n.createElement(V.qI.Provider,{value:E},n.createElement(V.aM.Provider,{value:z},I)))),!!j&&n.createElement("div",{className:"".concat(g,"-margin-offset"),style:{marginBottom:-j}}))}var Mt="__SPLIT__",qt=(0,st.b)("success","warning","error","validating",""),Lt=n.memo(function(e){var t=e.children;return t},function(e,t){return e.value===t.value&&e.update===t.update&&e.childProps.length===t.childProps.length&&e.childProps.every(function(a,r){return a===t.childProps[r]})});function Pt(e){return e!=null}function qe(){return{errors:[],warnings:[],touched:!1,validating:!1,name:[]}}function Vt(e){var t=e.name,a=e.noStyle,r=e.dependencies,l=e.prefixCls,i=e.shouldUpdate,s=e.rules,o=e.children,m=e.required,d=e.label,Z=e.messageVariables,h=e.trigger,I=h===void 0?"onChange":h,y=e.validateTrigger,C=e.hidden,E=(0,n.useContext)(ce.E_),T=E.getPrefixCls,g=(0,n.useContext)(V.q3),p=g.name,N=typeof o=="function",F=(0,n.useContext)(V.qI),x=(0,n.useContext)(se.FieldContext),P=x.validateTrigger,R=y!==void 0?y:P,S=Pt(t),w=T("form",l),f=n.useContext(se.ListContext),j=n.useRef(),$=ut({}),c=(0,J.Z)($,2),v=c[0],z=c[1],Q=(0,lt.Z)(function(){return qe()}),L=(0,J.Z)(Q,2),W=L[0],ue=L[1],le=function(O){var k=f==null?void 0:f.getKey(O.name);if(ue(O.destroy?qe():O,!0),a&&F){var Y=O.name;if(O.destroy)Y=j.current||Y;else if(k!==void 0){var H=(0,J.Z)(k,2),oe=H[0],ae=H[1];Y=[oe].concat((0,q.Z)(ae)),j.current=Y}F(O,Y)}},D=function(O,k){z(function(Y){var H=(0,b.Z)({},Y),oe=[].concat((0,q.Z)(O.name.slice(0,-1)),(0,q.Z)(k)),ae=oe.join(Mt);return O.destroy?delete H[ae]:H[ae]=O,H})},re=n.useMemo(function(){var A=(0,q.Z)(W.errors),O=(0,q.Z)(W.warnings);return Object.values(v).forEach(function(k){A.push.apply(A,(0,q.Z)(k.errors||[])),O.push.apply(O,(0,q.Z)(k.warnings||[]))}),[A,O]},[v,W.errors,W.warnings]),ne=(0,J.Z)(re,2),B=ne[0],ie=ne[1],K=ct();function U(A,O,k){return a&&!C?A:n.createElement(Ot,(0,b.Z)({key:"row"},e,{prefixCls:w,fieldId:O,isRequired:k,errors:B,warnings:ie,meta:W,onSubItemMetaChange:D}),A)}if(!S&&!N&&!r)return U(o);var _={};return typeof d=="string"?_.label=d:t&&(_.label=String(t)),Z&&(_=(0,b.Z)((0,b.Z)({},_),Z)),n.createElement(se.Field,(0,b.Z)({},e,{messageVariables:_,trigger:I,validateTrigger:R,onMetaChange:le}),function(A,O,k){var Y=fe(t).length&&O?O.name:[],H=je(Y,p),oe=m!==void 0?m:!!(s&&s.some(function(ee){if(ee&&(0,Ce.Z)(ee)==="object"&&ee.required&&!ee.warningOnly)return!0;if(typeof ee=="function"){var de=ee(k);return de&&de.required&&!de.warningOnly}return!1})),ae=(0,b.Z)({},A),me=null;if(Array.isArray(o)&&S)me=o;else if(!(N&&(!(i||r)||S))){if(!(r&&!N&&!S))if((0,ke.l$)(o)){var G=(0,b.Z)((0,b.Z)({},o.props),ae);if(G.id||(G.id=H),e.help||B.length>0||ie.length>0||e.extra){var Ie=[];(e.help||B.length>0)&&Ie.push("".concat(H,"_help")),e.extra&&Ie.push("".concat(H,"_extra")),G["aria-describedby"]=Ie.join(" ")}B.length>0&&(G["aria-invalid"]="true"),oe&&(G["aria-required"]="true"),(0,Ae.Yr)(o)&&(G.ref=K(Y,o));var zt=new Set([].concat((0,q.Z)(fe(I)),(0,q.Z)(fe(R))));zt.forEach(function(ee){G[ee]=function(){for(var de,De,Se,Ke,pe,Ue=arguments.length,Ne=new Array(Ue),ye=0;ye<Ue;ye++)Ne[ye]=arguments[ye];(Se=ae[ee])===null||Se===void 0||(de=Se).call.apply(de,[ae].concat(Ne)),(pe=(Ke=o.props)[ee])===null||pe===void 0||(De=pe).call.apply(De,[Ke].concat(Ne))}});var Ht=[G["aria-required"],G["aria-invalid"],G["aria-describedby"]];me=n.createElement(Lt,{value:ae[e.valuePropName||"value"],update:o,childProps:Ht},(0,ke.Tm)(o,G))}else N&&(i||r)&&!S?me=o(k):me=o}return U(me,H,oe)})}var Qe=Vt;Qe.useStatus=ot;var Tt=Qe,jt=function(e,t){var a={};for(var r in e)Object.prototype.hasOwnProperty.call(e,r)&&t.indexOf(r)<0&&(a[r]=e[r]);if(e!=null&&typeof Object.getOwnPropertySymbols=="function")for(var l=0,r=Object.getOwnPropertySymbols(e);l<r.length;l++)t.indexOf(r[l])<0&&Object.prototype.propertyIsEnumerable.call(e,r[l])&&(a[r[l]]=e[r[l]]);return a},Wt=function(t){var a=t.prefixCls,r=t.children,l=jt(t,["prefixCls","children"]),i=n.useContext(ce.E_),s=i.getPrefixCls,o=s("form",a),m=n.useMemo(function(){return{prefixCls:o,status:"error"}},[o]);return n.createElement(se.List,(0,b.Z)({},l),function(d,Z,h){return n.createElement(V.Rk.Provider,{value:m},r(d.map(function(I){return(0,b.Z)((0,b.Z)({},I),{fieldKey:I.key})}),Z,{errors:h.errors,warnings:h.warnings}))})},$t=Wt;function At(){var e=(0,n.useContext)(V.q3),t=e.form;return t}var te=at;te.Item=Tt,te.List=$t,te.ErrorList=we,te.useForm=$e,te.useFormInstance=At,te.useWatch=se.useWatch,te.Provider=V.RV,te.create=function(){};var kt=te}}]);