(self.webpackChunksurvey_king=self.webpackChunksurvey_king||[]).push([[1990],{10484:function(z,x,r){"use strict";r.r(x),r.d(x,{default:function(){return $}});var v=r(67294),p=r(36855),f=r(39428),F=r(3182),j=r(94657),C=r(27400),I=r(9761),n=r(1615),A=r(42285),b=r(69610),D=r(54941),y=r(3980),h=r(54531),E=function(){function o(t){(0,b.Z)(this,o),this.active="home",this.makeObservable(),this.getProfile()}return(0,D.Z)(o,[{key:"makeObservable",value:function(){(0,h.Ou)(this,{active:h.LO.ref,profile:h.LO,getProfile:h.aD})}},{key:"getProfile",value:function(){var s=this;y.hi.updateUserInfo().then(function(l){s.profile=l})}}]),o}(),B=v.createContext({});function k(){return(0,v.useContext)(B)}var e=r(85893),S=function(){var t=(0,C.a)(),s=t.user,l=(0,v.useState)(!1),m=(0,j.Z)(l,2),d=m[0],i=m[1],c=k();return c.active==="home"?(0,e.jsxs)(e.Fragment,{children:[(0,e.jsxs)(n.aV,{children:[(0,e.jsx)(n.aV.Item,{clickable:!0,onClick:function(){c.active="userInfo"},prefix:(0,e.jsx)(n.Ee,{src:s==null?void 0:s.avatar,style:{borderRadius:20},fit:"cover",width:40,height:40}),children:s==null?void 0:s.name}),(0,e.jsx)(n.aV.Item,{clickable:!0,onClick:function(){i(!0)},children:"\u9000\u51FA\u767B\u5F55"})]}),(0,e.jsx)(n.Vb,{visible:d,actions:[{text:"\u9000\u51FA",key:"exit",onClick:function(){var a=(0,F.Z)((0,f.Z)().mark(function u(){return(0,f.Z)().wrap(function(g){for(;;)switch(g.prev=g.next){case 0:t.logout(),A.m8.push("/user/login");case 2:case"end":return g.stop()}},u)}));function Z(){return a.apply(this,arguments)}return Z}()},{text:"\u53D6\u6D88",key:"save"}],onClose:function(){return i(!1)}})]}):(0,e.jsx)(e.Fragment,{})},V=(0,I.Pi)(S),P=r(11849),O=function(){var t=k(),s=(0,C.a)(),l=s.user,m=n.l0.useForm(),d=(0,j.Z)(m,1),i=d[0];return t.active!=="userInfo"?(0,e.jsx)(e.Fragment,{}):(0,e.jsxs)(e.Fragment,{children:[(0,e.jsx)(n.l2,{onBack:function(){t.active="home"},children:"\u4E2A\u4EBA\u4FE1\u606F"}),(0,e.jsxs)(n.l0,{form:i,layout:"horizontal",initialValues:l,footer:(0,e.jsx)(n.zx,{block:!0,type:"submit",color:"primary",size:"large",onClick:(0,F.Z)((0,f.Z)().mark(function c(){var a;return(0,f.Z)().wrap(function(u){for(;;)switch(u.prev=u.next){case 0:return u.next=2,y.hi.updateUserInfo((0,P.Z)((0,P.Z)({},l),i.getFieldsValue()));case 2:a=u.sent,a.success?(n.FN.show("\u4FEE\u6539\u6210\u529F"),t.getProfile()):n.FN.show({icon:"error",content:a.message});case 4:case"end":return u.stop()}},c)})),children:"\u63D0\u4EA4"}),children:[(0,e.jsx)(n.l0.Item,{name:"name",label:"\u59D3\u540D",rules:[{required:!0,message:"\u59D3\u540D\u4E0D\u80FD\u4E3A\u7A7A"}],children:(0,e.jsx)(n.II,{onChange:console.log,placeholder:"\u8BF7\u8F93\u5165\u59D3\u540D"})}),(0,e.jsx)(n.l0.Item,{name:"phone",label:"\u624B\u673A\u53F7",children:(0,e.jsx)(n.II,{onChange:console.log,placeholder:"\u8BF7\u8F93\u5165\u60A8\u7684\u8054\u7CFB\u7535\u8BDD"})}),(0,e.jsx)(n.l0.Item,{name:"email",label:"\u90AE\u7BB1",children:(0,e.jsx)(n.II,{onChange:console.log,placeholder:"\u8BF7\u8F93\u5165\u60A8\u7684\u90AE\u7BB1"})})]})]})},U=(0,I.Pi)(O),$=function(){var o=(0,v.useMemo)(function(){var t=p.parse(window.location.search),s=new E({});return s},[]);return(0,e.jsxs)(B.Provider,{value:o,children:[(0,e.jsx)(V,{}),(0,e.jsx)(U,{})]})}}}]);
