(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[4628],{70347:function(){},89239:function(V,S,e){"use strict";e.d(S,{xi:function(){return y},fQ:function(){return r},h8:function(){return Z},IR:function(){return U},gh:function(){return D},ZZ:function(){return p}});var h=e(67294),y=(0,h.createContext)({}),r=y.Provider,P=e(86582),B=e(98210),O=e(69646);function p(i){var c,s=[];return i==null||(c=i.children)===null||c===void 0||c.filter(function(l){return!(0,B.nj)(l.type)}).map(function(l){l.type==="QuestionSet"?s.push.apply(s,(0,P.Z)(p(l))):s.push({name:l.id,title:(0,O.WO)(l.title),schema:l,type:l.type==="Upload"?"image":"string"})}),s.length===0?s.push({name:"\u6682\u65E0\u6570\u636E!"}):(s.push({name:"totalTime",title:"\u7B54\u5377\u65F6\u957F"}),s.push({name:"deviceType",title:"\u8BBE\u5907\u7C7B\u578B"}),s.push({name:"platform",title:"\u5E73\u53F0\u7C7B\u578B"})),s}function Z(i){var c={};return i&&(i.id&&(c[i.id]=(0,O.WO)(i.title)),i.children&&i.children.forEach(function(s){return Object.assign(c,Z(s))})),c}function U(i,c,s){return i.map(function(l){var u={};return c.forEach(function(f){if(f.name==="totalTime"){u.totalTime=D(l.metaInfo.answerInfo.endTime-l.metaInfo.answerInfo.startTime);return}else if(f.name==="deviceType"){u.deviceType=l.metaInfo.clientInfo.deviceType;return}else if(f.name==="platform"){u.platform=l.metaInfo.clientInfo.platform;return}var n=f.schema||{},t=n.type,d=n.id,a=l.answer[d];if(!!a){var o=[];t==="Cascader"?o=z(a,n):t==="MatrixAuto"?o=F(a,n):t.startsWith("Matrix")?o=G(a,n):t==="Upload"?o=W(a,n,l.attachment):o=j(a,n),t==="Upload"?u[d]=o:u[d]=o.join(",")}}),u.id=l.id,u})}function j(i,c){var s,l=[];return(s=c.children)===null||s===void 0||s.forEach(function(u){var f=i[u.id];f===!0?l.push((0,O.WO)(u.title)):f&&l.push(f)}),l}function W(i,c,s){var l,u=[];return(l=c.children)===null||l===void 0||l.forEach(function(f){var n=i[f.id];u=s.filter(function(t){return n.includes(t.id)})}),u}function z(i,c){var s,l=[],u=c==null?void 0:c.dataSource;return u&&((s=c.children)===null||s===void 0||s.forEach(function(f){if(i.hasOwnProperty(f.id)){var n,t=(n=u)===null||n===void 0?void 0:n.find(function(d){return d.value===i[f.id]});t?(u=t.children,l.push(t.label)):l.push("")}})),l}function G(i,c){var s,l=[];return(s=c.row)===null||s===void 0||s.forEach(function(u){var f=u.id,n=(0,O.WO)(u.title);l.push("".concat(n,":(").concat(j(i[f],c),")"))}),l}function F(i,c){var s=[];return i.forEach(function(l){s.push("(".concat(j(l,c),")"))}),s}function D(i){var c=Math.floor(i/(36e5*24)),s=Math.floor(i/36e5%24),l=Math.floor(i/6e4%60),u=Math.floor(i/1e3%60),f="";return c>0&&(f+=c+"\u5929"),s>0&&(f+=s+"\u5C0F\u65F6"),l>0&&(f+=l+"\u5206\u949F"),u>0&&(f+=u+"\u79D2"),f}},32322:function(V,S,e){"use strict";e.d(S,{g:function(){return A}});var h=e(77576),y=e(12028),r=e(34792),P=e(48086),B=e(13062),O=e(71230),p=e(89032),Z=e(15746),U=e(58024),j=e(39144),W=e(22385),z=e(69713),G=e(57663),F=e(71577),D=e(2824),i=e(47673),c=e(4107),s=e(68628),l=e(77613),u=e(89239),f=e(67294),n=e(43711),t=e(85893),d=c.Z.Search,a=function(v){return[{title:"\u95EE\u5377\u603B\u6570",desc:(0,t.jsx)("span",{children:"\u95EE\u5377\u521B\u5EFA\u4E4B\u540E\u586B\u5199\u7684\u95EE\u5377\u6570\u91CF\u603B\u548C"}),total:(v==null?void 0:v.total)||0},{title:"\u4ECA\u65E5\u65B0\u589E",desc:(0,t.jsx)("span",{children:"\u8BE5\u6570\u636E\u6307\u7684\u662F\u4ECA\u65E5\u5185\uFF0C\u6240\u6709\u8BBF\u5BA2\u5728\u8BE5\u95EE\u5377\u5185\u63D0\u4EA4\u6570\u636E\u7684\u603B\u548C"}),total:(v==null?void 0:v.totalOfToday)||0},{title:"\u5E73\u5747\u586B\u5199\u65F6\u957F",desc:(0,t.jsx)("span",{children:"\u5E73\u5747\u586B\u5199\u65F6\u957F=\u5355\u6761\u6570\u636E\u586B\u5199\u65F6\u957F\u603B\u548C/\u6570\u636E\u6761\u6570"}),total:(0,u.gh)((v==null?void 0:v.averageDuration)||0)},{title:"\u6700\u8FD1\u66F4\u65B0\u65F6\u95F4",desc:(0,t.jsx)("span",{children:"\u6700\u8FD1\u4E00\u6B21\u95EE\u5377\u586B\u5199\u65F6\u95F4"}),total:v!=null&&v.lastUpdate?"".concat((0,u.gh)(new Date().getTime()-(v.lastUpdate||0)),"\u524D"):"\u672A\u5F00\u59CB"}]},o=(0,l.P)(function(){var g=(0,n.o)(),v=g.loading,x=(0,f.useState)(a(g.project)),C=(0,D.Z)(x,2),N=C[0],b=C[1];return(0,f.useEffect)(function(){var E=window.setInterval(function(){b(a(g.project))},1e3);return function(){return clearInterval(E)}},[]),(0,t.jsx)(O.Z,{gutter:[20,20],className:"data-info",children:N.map(function(E){return(0,t.jsx)(Z.Z,{sm:6,xs:24,children:(0,t.jsxs)(j.Z,{loading:v,children:[(0,t.jsx)("div",{className:"title",children:(0,t.jsx)(F.Z,{type:"link",className:"total",children:E.total})}),(0,t.jsxs)("div",{className:"desc",children:[E.title,(0,t.jsx)(z.Z,{overlay:E.desc,placement:"bottom",children:(0,t.jsx)(s.Z,{className:"desc-info"})})]})]})},E.title)})})}),A=(0,l.P)(function(){var g,v,x,C=(0,n.o)(),N=C.loading;return(0,t.jsxs)("div",{className:"survey-overview",children:[(0,t.jsx)("div",{className:"survey-title",children:C.name}),(0,t.jsx)(o,{}),(0,t.jsx)(O.Z,{gutter:[20,20],style:{marginTop:20},children:(0,t.jsx)(Z.Z,{sm:12,xs:24,children:(0,t.jsx)(j.Z,{loading:N,children:(0,t.jsxs)(O.Z,{gutter:[10,10],children:[(0,t.jsx)(Z.Z,{span:24,children:(0,t.jsx)(d,{value:"https://surveyking.cn/s/".concat(C.shortId),enterButton:"\u6253\u5F00",className:"open-target",onSearch:function(E){window.open("/s/".concat(C.shortId))}})}),(0,t.jsxs)(Z.Z,{className:"setting-item",span:24,children:[(0,t.jsx)("div",{className:"title",children:"\u95EE\u5377\u5F00\u542F/\u505C\u6B62"}),(0,t.jsx)("div",{children:(0,t.jsx)(y.Z,{defaultChecked:((g=C.project)===null||g===void 0?void 0:g.status)===1,onChange:function(E){C.saveOrUpdateProject({status:E?1:0}).then(function(M){M.code===200?E?P.default.info("\u95EE\u5377\u5DF2\u5F00\u542F"):P.default.warn("\u95EE\u5377\u5DF2\u505C\u6B62"):P.default.error(M.message)})}})})]}),(0,t.jsxs)(Z.Z,{className:"setting-item",span:24,children:[(0,t.jsx)("div",{className:"title",children:"\u95EE\u5377\u586B\u5199\u65F6\u81EA\u52A8\u4FDD\u5B58"}),(0,t.jsx)("div",{children:(0,t.jsx)(y.Z,{defaultChecked:(v=C.project)===null||v===void 0||(x=v.setting)===null||x===void 0?void 0:x.answerSetting.autoSave,onChange:function(E){C.updateSetting({answerSetting:{autoSave:E}}).then(function(M){M.code===200?E?P.default.info("\u6682\u5B58\u5DF2\u5F00\u542F"):P.default.warn("\u6682\u5B58\u5DF2\u505C\u6B62"):P.default.error(M.message)})}})})]})]})})})})]})}),I=function(){return _jsxs("svg",{viewBox:"0 0 1024 1024","p-id":"1192",width:"24",height:"24",children:[_jsx("path",{d:"M512 512m-512 0a512 512 0 1 0 1024 0 512 512 0 1 0-1024 0Z",fill:"#fff","p-id":"1193"}),_jsx("path",{d:"M634.8288 787.712a73.8304 73.8304 0 0 1-64.9216-72.6016 75.9296 75.9296 0 0 1 66.56-88.5248 8.6016 8.6016 0 0 0 0-17.152h-346.7264v-344.4224a74.1376 74.1376 0 0 1 59.8016-60.2112h380.0576a69.6832 69.6832 0 0 1 75.264 73.8304 47.6672 47.6672 0 0 1-15.36 31.0784h-69.5296v417.7408a74.1376 74.1376 0 0 1-59.8016 60.2112z m-46.08 0h-312.7808a79.8208 79.8208 0 0 1-71.3728-79.6672 83.2512 83.2512 0 0 1 70.8608-81.5104h305.7664a117.4016 117.4016 0 0 0-28.5184 89.856 100.4032 100.4032 0 0 0 36.1472 71.2704z m15.616-428.5952h-113.8688c-6.5024 0-11.6224 7.8336-11.6224 17.6128s5.12 17.664 11.6224 17.664h113.8688c6.5024 0 11.6224-7.8336 11.6224-17.664s-5.12-17.6128-11.6224-17.6128z m0 136.192h-113.8688c-6.5024 0-11.6224 7.8336-11.6224 17.664s5.12 17.6128 11.6224 17.6128h113.8688c6.5024 0 11.6224-7.8336 11.6224-17.6128s-5.12-17.664-11.6224-17.664z m-194.0992-153.2928a34.304 34.304 0 1 0 34.304 34.304 34.304 34.304 0 0 0-34.048-34.3552z m0 137.1648a34.304 34.304 0 1 0 34.304 34.304 34.304 34.304 0 0 0-34.048-34.4064z m357.6832-202.3936c0-25.1904-7.7824-36.864-25.0368-36.864s-25.1392 11.5712-25.1392 36.864z",fill:"black","p-id":"1194"})]})},L=function(v){return _jsx(Icon,_objectSpread({component:I},v))}},89247:function(V,S,e){"use strict";e.r(S),e.d(S,{Overview:function(){return Z},SurveyContext:function(){return p.ZX},SurveyDataProvider:function(){return p.fQ},api:function(){return p.hi}});var h=e(11849),y=e(51615),r=e(77613),P=e(22048),B=e(32322),O=e(85893),p=e(78336),Z=(0,r.P)(function(U){var j=(0,y.UO)(),W=j.shortId;return W==="new"?(0,O.jsx)(P.default,(0,h.Z)({},U)):(0,O.jsx)(B.g,{})});S.default=Z},39144:function(V,S,e){"use strict";e.d(S,{Z:function(){return f}});var h=e(96156),y=e(22122),r=e(67294),P=e(94184),B=e.n(P),O=e(98423),p=e(65632),Z=function(n,t){var d={};for(var a in n)Object.prototype.hasOwnProperty.call(n,a)&&t.indexOf(a)<0&&(d[a]=n[a]);if(n!=null&&typeof Object.getOwnPropertySymbols=="function")for(var o=0,a=Object.getOwnPropertySymbols(n);o<a.length;o++)t.indexOf(a[o])<0&&Object.prototype.propertyIsEnumerable.call(n,a[o])&&(d[a[o]]=n[a[o]]);return d},U=function(t){var d=t.prefixCls,a=t.className,o=t.hoverable,A=o===void 0?!0:o,I=Z(t,["prefixCls","className","hoverable"]);return r.createElement(p.C,null,function(L){var g=L.getPrefixCls,v=g("card",d),x=B()("".concat(v,"-grid"),a,(0,h.Z)({},"".concat(v,"-grid-hoverable"),A));return r.createElement("div",(0,y.Z)({},I,{className:x}))})},j=U,W=function(n,t){var d={};for(var a in n)Object.prototype.hasOwnProperty.call(n,a)&&t.indexOf(a)<0&&(d[a]=n[a]);if(n!=null&&typeof Object.getOwnPropertySymbols=="function")for(var o=0,a=Object.getOwnPropertySymbols(n);o<a.length;o++)t.indexOf(a[o])<0&&Object.prototype.propertyIsEnumerable.call(n,a[o])&&(d[a[o]]=n[a[o]]);return d},z=function(t){return r.createElement(p.C,null,function(d){var a=d.getPrefixCls,o=t.prefixCls,A=t.className,I=t.avatar,L=t.title,g=t.description,v=W(t,["prefixCls","className","avatar","title","description"]),x=a("card",o),C=B()("".concat(x,"-meta"),A),N=I?r.createElement("div",{className:"".concat(x,"-meta-avatar")},I):null,b=L?r.createElement("div",{className:"".concat(x,"-meta-title")},L):null,E=g?r.createElement("div",{className:"".concat(x,"-meta-description")},g):null,M=b||E?r.createElement("div",{className:"".concat(x,"-meta-detail")},b,E):null;return r.createElement("div",(0,y.Z)({},v,{className:C}),N,M)})},G=z,F=e(51752),D=e(71230),i=e(15746),c=e(97647),s=function(n,t){var d={};for(var a in n)Object.prototype.hasOwnProperty.call(n,a)&&t.indexOf(a)<0&&(d[a]=n[a]);if(n!=null&&typeof Object.getOwnPropertySymbols=="function")for(var o=0,a=Object.getOwnPropertySymbols(n);o<a.length;o++)t.indexOf(a[o])<0&&Object.prototype.propertyIsEnumerable.call(n,a[o])&&(d[a[o]]=n[a[o]]);return d};function l(n){var t=n.map(function(d,a){return r.createElement("li",{style:{width:"".concat(100/n.length,"%")},key:"action-".concat(a)},r.createElement("span",null,d))});return t}var u=function(t){var d,a,o=r.useContext(p.E_),A=o.getPrefixCls,I=o.direction,L=r.useContext(c.Z),g=function(Q){var K;(K=t.onTabChange)===null||K===void 0||K.call(t,Q)},v=function(){var Q;return r.Children.forEach(t.children,function(K){K&&K.type&&K.type===j&&(Q=!0)}),Q},x=t.prefixCls,C=t.className,N=t.extra,b=t.headStyle,E=b===void 0?{}:b,M=t.bodyStyle,H=M===void 0?{}:M,X=t.title,Y=t.loading,w=t.bordered,lt=w===void 0?!0:w,it=t.size,k=t.type,_=t.cover,J=t.actions,R=t.tabList,ot=t.children,q=t.activeTabKey,st=t.defaultActiveTabKey,ct=t.tabBarExtraContent,ut=t.hoverable,tt=t.tabProps,dt=tt===void 0?{}:tt,vt=s(t,["prefixCls","className","extra","headStyle","bodyStyle","title","loading","bordered","size","type","cover","actions","tabList","children","activeTabKey","defaultActiveTabKey","tabBarExtraContent","hoverable","tabProps"]),m=A("card",x),ft=H.padding===0||H.padding==="0px"?{padding:24}:void 0,T=r.createElement("div",{className:"".concat(m,"-loading-block")}),mt=r.createElement("div",{className:"".concat(m,"-loading-content"),style:ft},r.createElement(D.Z,{gutter:8},r.createElement(i.Z,{span:22},T)),r.createElement(D.Z,{gutter:8},r.createElement(i.Z,{span:8},T),r.createElement(i.Z,{span:15},T)),r.createElement(D.Z,{gutter:8},r.createElement(i.Z,{span:6},T),r.createElement(i.Z,{span:18},T)),r.createElement(D.Z,{gutter:8},r.createElement(i.Z,{span:13},T),r.createElement(i.Z,{span:9},T)),r.createElement(D.Z,{gutter:8},r.createElement(i.Z,{span:4},T),r.createElement(i.Z,{span:3},T),r.createElement(i.Z,{span:16},T))),et=q!==void 0,ht=(0,y.Z)((0,y.Z)({},dt),(d={},(0,h.Z)(d,et?"activeKey":"defaultActiveKey",et?q:st),(0,h.Z)(d,"tabBarExtraContent",ct),d)),at,nt=R&&R.length?r.createElement(F.Z,(0,y.Z)({size:"large"},ht,{className:"".concat(m,"-head-tabs"),onChange:g}),R.map(function($){return r.createElement(F.Z.TabPane,{tab:$.tab,disabled:$.disabled,key:$.key})})):null;(X||N||nt)&&(at=r.createElement("div",{className:"".concat(m,"-head"),style:E},r.createElement("div",{className:"".concat(m,"-head-wrapper")},X&&r.createElement("div",{className:"".concat(m,"-head-title")},X),N&&r.createElement("div",{className:"".concat(m,"-extra")},N)),nt));var Et=_?r.createElement("div",{className:"".concat(m,"-cover")},_):null,gt=r.createElement("div",{className:"".concat(m,"-body"),style:H},Y?mt:ot),yt=J&&J.length?r.createElement("ul",{className:"".concat(m,"-actions")},l(J)):null,xt=(0,O.Z)(vt,["onTabChange"]),rt=it||L,Ct=B()(m,(a={},(0,h.Z)(a,"".concat(m,"-loading"),Y),(0,h.Z)(a,"".concat(m,"-bordered"),lt),(0,h.Z)(a,"".concat(m,"-hoverable"),ut),(0,h.Z)(a,"".concat(m,"-contain-grid"),v()),(0,h.Z)(a,"".concat(m,"-contain-tabs"),R&&R.length),(0,h.Z)(a,"".concat(m,"-").concat(rt),rt),(0,h.Z)(a,"".concat(m,"-type-").concat(k),!!k),(0,h.Z)(a,"".concat(m,"-rtl"),I==="rtl"),a),C);return r.createElement("div",(0,y.Z)({},xt,{className:Ct}),at,Et,gt,yt)};u.Grid=j,u.Meta=G;var f=u},58024:function(V,S,e){"use strict";var h=e(65056),y=e.n(h),r=e(70347),P=e.n(r),B=e(18106),O=e(13062),p=e(89032)}}]);