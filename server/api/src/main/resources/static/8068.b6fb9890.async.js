(self.webpackChunksurvey_king=self.webpackChunksurvey_king||[]).push([[8068],{68068:function(ae,X,o){"use strict";o.d(X,{O:function(){return re},Z:function(){return Oe}});var R=o(9761),M=o(67294),G=o(3044),k=o(86052),K=o(48187),Y=o(91220),q=o(93224),Z=o(39428),j=o(83279),$=o(3182),V=o(11849),ee=o(94657),se=o(69610),ue=o(54941),_=o(3980),T=o(64031),p=o(54531);function le(u,e){}var O=o(40807),oe=["visible"],de=function(){function u(e){var r=this,i;(0,se.Z)(this,u),this.props=void 0,this.progress=0,this.form=void 0,this.pagination={current:1,pageSize:1,currentIndex:0,paginationNodes:[]},this.unmount=!1,this.notNoneInQuestionGroup=[],this.answerSheet=[],this.answerSheetVisible=!1,this.reviewAnswer={},this.autoNextPage=void 0,this.compute=void 0,this.hiddenOption={},this.visibleId=void 0,this.lazy=!1,this.preloadId=void 0;var n=e.initialValues,t=e.readOnly,a=e.fieldPermission,s=e.theme,l=e.schema,d=e.mode,h=e.onePageOneQuestion,c=h===void 0?!1:h,y=e.paginationVisible;this.props=e,this.lazy=!!e.onLazyLoad,this.initPagination(l,y,c);var v=(0,T.Np)({initialValues:n,readOnly:t,validateFirst:!0,pattern:e.pattern,effects:function(m){(0,T.dQ)(function(){if(a){var f=[],b=[];Object.entries(a).forEach(function(P){var x=(0,ee.Z)(P,2),F=x[0],A=x[1];A===0?f.push(F):A===1&&b.push(F)}),f.length>0&&v.setFieldState("*(".concat(f.join(","),")"),function(P){return P.display="none"}),b.length>0&&v.setFieldState("*(".concat(b.join(","),")"),function(P){return P.pattern="readPretty"})}r.handleFormValuesChange(),r.highlightJs(),r.setVisible(0)}),(0,T.pO)(function(f){r.handleFormValuesChange()}),(0,T.Zj)("*",function(f,b){r.handleFieldValueChange(f,b)}),le(l,m)}});this.form=v,this.answerSheet=((i=this.lazy?l.children:(0,O.Xn)(l))===null||i===void 0?void 0:i.filter(function(g){var m,f;return r.lazy||(0,O.a1)(g.type)&&((m=g.attribute)===null||m===void 0?void 0:m.display)!=="hidden"&&((f=g.attribute)===null||f===void 0?void 0:f.examAnswerMode)!=="none"}).map(function(g){var m,f=(m=e.initialQuestionScore)===null||m===void 0?void 0:m[g.id];return{finished:!1,id:g.id,marked:!1,display:r.lazy?"hidden":"visible",favorite:!!e.wrongMode,isCorrect:f===void 0?void 0:f>0}}))||[],s==="antd"&&d==="exam"&&(this.answerSheetVisible=!0),this.autoNextPage=0,e.exerciseMode&&e.onePageOneQuestion&&(this.autoNextPage=1),this.makeObservable(),this.props.correctAnswerVisible&&n&&(0,O.Xn)(l).forEach(function(g){r.computeReviewAnswer(g,n[g.id]).then(function(m){m&&(r.reviewAnswer[g.id].visible=!0)})}),this.compute=fe(function(){r.unmount||(r.lazy||r.reOrder(),r.computePagination())},10)}return(0,ue.Z)(u,[{key:"makeObservable",value:function(){(0,p.Ou)(this,{progress:p.LO.ref,pagination:p.LO,form:p.LO.ref,props:p.LO.shallow,answerSheet:p.LO,answerSheetVisible:p.LO.ref,reviewAnswer:p.LO,notNoneInQuestionGroup:p.LO,schema:p.LO.computed,autoNextPage:p.LO.ref,hiddenOption:p.LO.shallow,exerciseMode:p.LO.computed,lazy:p.LO.ref,visibleId:p.LO.ref,computeProgress:p.aD,validate:p.aD,handlePrev:p.aD,handleNext:p.aD,submit:p.aD,handleFormValuesChange:p.aD,changePageIndex:p.aD,handleFieldValueChange:p.aD,computeReviewAnswer:p.aD,reOrder:p.aD})}},{key:"schema",get:function(){return this.props.schema}},{key:"questionsGroupByPagination",get:function(){return this.pagination.paginationNodes}},{key:"exerciseMode",get:function(){return this.props.exerciseMode||!1}},{key:"initPagination",value:function(r){var i=arguments.length>1&&arguments[1]!==void 0?arguments[1]:!0,n=arguments.length>2&&arguments[2]!==void 0?arguments[2]:!1;i?this.pagination.paginationNodes=(0,O._T)(r,n,this.lazy):this.pagination.paginationNodes=[r.children],this.pagination.pageSize=this.pagination.paginationNodes.length}},{key:"computePagination",value:function(){if(this.props.paginationVisible===!1)return;var r=this.form.fields,i=this.pagination,n=i.paginationNodes,t=i.currentIndex;if(this.lazy){this.pagination=(0,V.Z)((0,V.Z)({},this.pagination),{},{pageSize:n.length,currentIndex:t,current:t+1});return}function a(c){return n[c].every(function(y){var v=r[y.id].data;return v.some(function(g){return g==="self_display"||g.startsWith("rule_")})})}for(var s=0,l=0,d=0;d<n.length;d++){var h=n[d];if(h.some(function(c){var y=r[c.id].data;return y.length===0}))l++,s++;else{if(a(d))continue;l++,d<=t&&s++}}this.pagination=(0,V.Z)((0,V.Z)({},this.pagination),{},{current:s,pageSize:l,currentIndex:t})}},{key:"highlightJs",value:function(){document.querySelectorAll(".ql-syntax").forEach(function(r){})}},{key:"validate",value:function(){var e=(0,$.Z)((0,Z.Z)().mark(function i(n){var t,a;return(0,Z.Z)().wrap(function(l){for(;;)switch(l.prev=l.next){case 0:if(a=((t=this.questionsGroupByPagination[n])===null||t===void 0?void 0:t.reduce(function(d,h){return d.push.apply(d,(0,j.Z)((0,O.Xn)(h))),d},[]))||[],a.length!==0){l.next=3;break}return l.abrupt("return",!0);case 3:return l.abrupt("return",this.form.validate("*(".concat(a.map(function(d){return d.id}).join(","),")")));case 4:case"end":return l.stop()}},i,this)}));function r(i){return e.apply(this,arguments)}return r}()},{key:"setVisible",value:function(r){var i=this;if(this.props.paginationVisible===!1)return!1;var n=this.pagination,t=n.currentIndex,a=n.paginationNodes,s=this.form.fields,l=r===void 0?t:r;function d(x){return a[x].every(function(F){var A=s[F.id].data;return A.some(function(B){return B==="self_display"||B.startsWith("rule_")})})}if(r!==void 0){if(r===t+1){var h=d(r);if(h){this.pagination.currentIndex=t+1,this.setVisible(r+1);return}}else if(r===t-1){var c=d(r);if(c){this.pagination.currentIndex=t-1,this.setVisible(r-1);return}}}if(this.lazy){var y;this.visibleId&&(s[this.visibleId].display="hidden");var v=a[l];v==null||v.forEach(function(x){s[x.id].display="visible",i.visibleId=x.id});var g=(y=a[l+1])===null||y===void 0?void 0:y[0].id;if(g){var m,f;(m=this.form)===null||m===void 0||(f=m.query(g).take())===null||f===void 0||f.setComponentProps({preload:!0})}this.compute()}else for(var b=0;b<a.length;b++){var P=a[b];b===l?P==null||P.forEach(function(x){var F=s[x.id].data;s[x.id].data=F.filter(function(A){return A!=="hidden"})}):P.forEach(function(x){var F=s[x.id].data;F.includes("hidden")||(s[x.id].data=[].concat((0,j.Z)(F),["hidden"]))})}this.pagination.currentIndex=l}},{key:"handlePrev",value:function(){var r=this.pagination.currentIndex;this.setVisible(r-1)}},{key:"handleNext",value:function(){var e=(0,$.Z)((0,Z.Z)().mark(function i(n){var t;return(0,Z.Z)().wrap(function(s){for(;;)switch(s.prev=s.next){case 0:return t=this.pagination.currentIndex,s.prev=1,s.next=4,this.validate(t);case 4:this.setVisible(t+1),setTimeout(function(){return window.scroll({top:0})},10),s.next=11;break;case 8:s.prev=8,s.t0=s.catch(1),console.log(s.t0);case 11:case"end":return s.stop()}},i,this,[[1,8]])}));function r(i){return e.apply(this,arguments)}return r}()},{key:"getPageIndexByQid",value:function(r){return this.lazy?this.questionsGroupByPagination.findIndex(function(i){return i[0].id===r}):this.questionsGroupByPagination.findIndex(function(i){return!!(i.reduce(function(n,t){return n.push.apply(n,(0,j.Z)((0,O.Xn)(t))),n},[])||[]).find(function(n){return n.id===r})})}},{key:"changePageIndex",value:function(r){var i=this.getPageIndexByQid(r);i!==-1&&(this.setVisible(i),(0,O.lA)(r,this.props.progressBar?-25:0))}},{key:"handleFormValuesChange",value:function(){var e=(0,$.Z)((0,Z.Z)().mark(function i(){return(0,Z.Z)().wrap(function(t){for(;;)switch(t.prev=t.next){case 0:this.computeProgress(),this.props.onChange&&this.props.onChange(this.form.values,this.progress);case 2:case"end":return t.stop()}},i,this)}));function r(){return e.apply(this,arguments)}return r}()},{key:"handleFieldValueChange",value:function(){var e=(0,$.Z)((0,Z.Z)().mark(function i(n,t){var a,s,l;return(0,Z.Z)().wrap(function(h){for(;;)switch(h.prev=h.next){case 0:l=n.path.toString(),(a=(s=this.props).onFieldChange)===null||a===void 0||a.call(s,l,(0,p.ZN)(n.value)),this.props.exerciseMode&&this.computeReviewAnswer(n.componentProps.schema,n.value);case 3:case"end":return h.stop()}},i,this)}));function r(i,n){return e.apply(this,arguments)}return r}()},{key:"computeReviewAnswer",value:function(){var e=(0,$.Z)((0,Z.Z)().mark(function i(n,t){var a,s,l,d,h,c,y,v;return(0,Z.Z)().wrap(function(m){for(;;)switch(m.prev=m.next){case 0:if(a=!1,this.props.mode!=="exam"){m.next=12;break}if(l=this.reviewAnswer[n.id]||{},d=n.type,["FillBlank","MultipleBlank","HorzBlank","Radio","Checkbox","Judge"].includes(n.type)){m.next=6;break}return m.abrupt("return");case 6:if((d==="Radio"||d==="Judge")&&(l.visible=!0),(s=n.attribute)!==null&&s!==void 0&&s.examAnswerMode&&["selectAll","selectCorrect","onlyOne","select"].includes(n.attribute.examAnswerMode)){m.next=9;break}return m.abrupt("return");case 9:a=!0,this.reviewAnswer[n.id]=n.children.reduce(function(f,b){var P,x=!!((P=b.attribute)!==null&&P!==void 0&&P.examCorrectAnswer);if(d==="FillBlank"||d==="MultipleBlank"||d==="Textarea"||d==="HorzBlank"){var F,A,B=(F=b.attribute)===null||F===void 0?void 0:F.examMatchRule,W=t==null?void 0:t[b.id],J=(A=b.attribute)===null||A===void 0?void 0:A.examCorrectAnswer;!W||!J?x=!1:B==="contain"&&W.includes(J.trim())||W===J?x=!0:x=!1}return f[b.id]={selected:!!(t!=null&&t.hasOwnProperty(b.id)),isCorrect:x},f},{visible:!!(l!=null&&l.visible)}),this.autoNextPage===1&&(h=this.reviewAnswer[n.id],c=h.visible,y=(0,q.Z)(h,oe),v=Object.values(y).filter(function(f){return f.selected&&f.isCorrect}).length===1,v&&(d==="Radio"||d==="Judge")&&this.handleNext());case 12:return m.abrupt("return",a);case 13:case"end":return m.stop()}},i,this)}));function r(i,n){return e.apply(this,arguments)}return r}()},{key:"computeProgress",value:function(){var r=this;if(!!this.props.progressBar){var i=0,n=0;if(this.lazy){var t;i=((t=this.schema.children)===null||t===void 0?void 0:t.length)||1,n=Object.keys(this.form.values).length,this.progress=n/i;return}this.form.query("*").forEach(function(a){var s=r.answerSheet.find(function(l){return l.id===a.path.toString()});a.displayName==="Field"&&!a.path.toString().startsWith("_")&&(s&&(s.display=a.display),a.data.filter(function(l){return l!=="hidden"}).length===0&&i++,!(0,O.xb)(a.value)&&a.componentProps.schema.attribute.display!=="hidden"?(n++,s&&(s.finished=!0)):s&&(s.finished=!1))}),this.progress=n/i}}},{key:"reOrder",value:function(){var r,i=this,n=1;(r=this.schema.children)===null||r===void 0||r.filter(function(t){var a;return(!(0,O.nj)(t.type)||t.type==="QuestionSet")&&((a=t.attribute)===null||a===void 0?void 0:a.examAnswerMode)!=="none"}).forEach(function(t){var a,s=t.id,l=i.form.query(s).get("display");if(l!=="none"&&((a=t.attribute)===null||a===void 0?void 0:a.display)!=="hidden"){var d=i.form.query(s).take();d&&(d.setDecoratorProps((0,V.Z)((0,V.Z)({},d.decoratorProps),{},{seqNo:n})),n++);var h=1;if(t.type==="QuestionSet"){var c;(c=t.children)===null||c===void 0||c.filter(function(y){return!(0,O.nj)(y.type)}).forEach(function(y){var v=y.id,g=i.form.query(v).get("display");if(g==="visible"){var m=i.form.query(v).take();m&&(m.setDecoratorProps((0,V.Z)((0,V.Z)({},m.decoratorProps),{},{seqNo:h})),h++)}})}}})}},{key:"submit",value:function(){var e=(0,$.Z)((0,Z.Z)().mark(function i(){var n,t,a,s,l,d,h,c,y,v,g;return(0,Z.Z)().wrap(function(f){for(;;)switch(f.prev=f.next){case 0:n="",t=this.form.query("*").map().filter(function(b){return!(0,T.JF)(b)}),a=(0,Y.Z)(t),f.prev=3,a.s();case 5:if((s=a.n()).done){f.next=31;break}if(c=s.value,!(0,T.JF)(c)){f.next=9;break}return f.abrupt("continue",29);case 9:if(n=c.path.toString(),y=((l=this.props.schema.children)===null||l===void 0||(d=l.find(function(b){return b.id===n}))===null||d===void 0||(h=d.attribute)===null||h===void 0?void 0:h.display)==="hidden",!y){f.next=13;break}return f.abrupt("continue",29);case 13:if(v=c.display,v!=="none"){f.next=16;break}return f.abrupt("continue",29);case 16:return g=c.selfDisplay,c.display="visible",f.prev=18,f.next=21,c.validate();case 21:c.display=g,f.next=29;break;case 24:return f.prev=24,f.t0=f.catch(18),this.changePageIndex(n),(0,O.lA)(n,this.props.progressBar?-25:0),f.abrupt("return");case 29:f.next=5;break;case 31:f.next=36;break;case 33:f.prev=33,f.t1=f.catch(3),a.e(f.t1);case 36:return f.prev=36,a.f(),f.finish(36);case 39:return this.props.onSubmit&&this.props.onSubmit((0,p.ZN)(this.form.values)),f.abrupt("return",{values:(0,p.ZN)(this.form.values)});case 41:case"end":return f.stop()}},i,this,[[3,33,36,39],[18,24]])}));function r(){return e.apply(this,arguments)}return r}()},{key:"markQuestion",value:function(r){var i=arguments.length>1&&arguments[1]!==void 0?arguments[1]:!0,n=this.answerSheet,t=n.findIndex(function(a){return a.id===r});this.answerSheet.splice(t,1,(0,V.Z)((0,V.Z)({},n[t]),{},{marked:i}))}},{key:"favoriteQuestion",value:function(r){var i=this,n=arguments.length>1&&arguments[1]!==void 0?arguments[1]:!0,t=this.answerSheet,a=t.findIndex(function(s){return s.id===r.templateId});n?_.hi.createUserBook((0,V.Z)((0,V.Z)({},r),{},{type:2})).then(function(s){s.success&&i.answerSheet.splice(a,1,(0,V.Z)((0,V.Z)({},t[a]),{},{favorite:n}))}):_.hi.deleteUserBook((0,V.Z)((0,V.Z)({},r),{},{type:2})).then(function(s){s.success&&i.answerSheet.splice(a,1,(0,V.Z)((0,V.Z)({},t[a]),{},{favorite:n}))})}},{key:"cleanup",value:function(){this.unmount=!0}}]),u}();function fe(u,e,r){var i=void 0,n=void 0;return function(){var t=+new Date;n||(n=t),r&&t-n>r?(u(),n=t,window.clearTimeout(i)):(window.clearTimeout(i),i=window.setTimeout(function(){u(),n=void 0},e))}}var te=o(99439),he=o(27484),ce=o.n(he),ve=o(90019),Q=o(10192),me=o(76826);function ge(u,e){var r,i,n=e.id,t=[];if((r=e.parent)!==null&&r!==void 0&&(i=r.type)!==null&&i!==void 0&&i.startsWith("Matrix"))if(e.parent.type==="MatrixAuto"&&Array.isArray(u))u.forEach(function(d,h){t.push(["".concat(h,"-").concat(n),d[e.id]])});else{var a;(a=e.parent.row)===null||a===void 0||a.forEach(function(d){u[d.id]?t.push(["".concat(d.id,"-").concat(n),u[d.id][e.id]]):t.push(["".concat(d.id,"-").concat(n),void 0])})}else{var s,l=(s=e.parent)===null||s===void 0?void 0:s.type;l==="Radio"||l==="Checkbox"?Object.keys(u).includes(e.id)&&t.push([e.id,u[e.id]]):t.push([e.id,u[e.id]])}return t}function ie(u){return!isNaN(parseFloat(u))&&isFinite(u)}function pe(u){return!!(ie(u)&&Number.isInteger(parseFloat(u)))}function N(u){var e=u.errMsg,r=u.schema,i=u.regEx,n=u.value;return r?S({schema:r,value:n,errMsg:e,isValid:function(a){return a?i.test("".concat(a)):!0}}):null}function ye(u){var e,r=u.schema,i=u.value,n=u.errMsg,t=((e=r.children)===null||e===void 0?void 0:e.filter(function(d){var h;return(h=d.attribute)===null||h===void 0?void 0:h.exclusiveFillBlankInMatrix}))||[],a=JSON.parse(JSON.stringify(i));Object.values(a).forEach(function(d){t.map(function(h){return h.id}).forEach(function(h){delete d[h]})});var s=!1;if(r.type==="MatrixAuto")i&&i.length>0&&(s=!0);else{var l;s=(l=r.row)===null||l===void 0?void 0:l.every(function(d){var h=a[d.id];return!(0,O.xb)(h)})}return s?null:n}function S(u){var e=u.value,r=u.schema,i=u.isValid,n=u.errMsg;return!r||!r.parent?null:ge(e,r).map(function(t){var a=(0,ee.Z)(t,2),s=a[0],l=a[1];return i(l)?null:"".concat(s,"^").concat(n)}).filter(function(t){return t})[0]}function $e(u){if(Object.keys(u).length===0)return!0}function be(u){var e=u.substring(6,8),r=u.substring(8,10),i=u.substring(10,12),n=new Date(parseFloat(e),parseFloat(r)-1,parseFloat(i));return!(n.getFullYear()-1900!==parseFloat(e)||n.getMonth()!==parseFloat(r)-1||n.getDate()!==parseFloat(i))}function Ve(u){var e=u;if(u.length!==15&&u.length!==18)return!1;if(u.length===15)return be(e);var r={11:"\u5317\u4EAC",12:"\u5929\u6D25",13:"\u6CB3\u5317",14:"\u5C71\u897F",15:"\u5185\u8499\u53E4",21:"\u8FBD\u5B81",22:"\u5409\u6797",23:"\u9ED1\u9F99\u6C5F ",31:"\u4E0A\u6D77",32:"\u6C5F\u82CF",33:"\u6D59\u6C5F",34:"\u5B89\u5FBD",35:"\u798F\u5EFA",36:"\u6C5F\u897F",37:"\u5C71\u4E1C",41:"\u6CB3\u5357",42:"\u6E56\u5317 ",43:"\u6E56\u5357",44:"\u5E7F\u4E1C",45:"\u5E7F\u897F",46:"\u6D77\u5357",50:"\u91CD\u5E86",51:"\u56DB\u5DDD",52:"\u8D35\u5DDE",53:"\u4E91\u5357",54:"\u897F\u85CF ",61:"\u9655\u897F",62:"\u7518\u8083",63:"\u9752\u6D77",64:"\u5B81\u590F",65:"\u65B0\u7586",71:"\u53F0\u6E7E",81:"\u9999\u6E2F",82:"\u6FB3\u95E8",91:"\u56FD\u5916 "},i=!0;if(!e||!/^([1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx])|([1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2})$/i.test(e))i=!1;else if(!r[e.substr(0,2)])i=!1;else if(e.length>18)i=!1;else if(e.length===18){for(var n=e.split(""),t=[7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2],a=[1,0,"X",9,8,7,6,5,4,3,2],s=0,l=0,d=0,h=0;h<17;h+=1)l=parseInt(n[h],10),d=t[h],s+=l*d;var c=n[17].toUpperCase()==="X"?"X":Number(n[17]);a[s%11]!==c&&(i=!1)}return i}function E(u,e){var r,i,n;return!u||!e?!0:((r=e.parent)===null||r===void 0?void 0:r.type)==="MatrixAuto"?u.every(function(t){return(0,O.xb)(t[e.id])}):(i=e.parent)!==null&&i!==void 0&&(n=i.type)!==null&&n!==void 0&&n.startsWith("Matrix")?Object.values(u).every(function(t){return(0,O.xb)(t[e.id])}):!!(0,O.xb)(u[e.id])}var xe={required:function(e,r){var i,n,t=r.schema,a=r.theme,s=r.survey;if(r&&r.required===!1)return null;if((0,O.xb)(e)){var l;return a==="antdForm"?"\u8BF7\u8F93\u5165":(s==null||(l=s.attribute)===null||l===void 0?void 0:l.validateMsgQuestionRequired)||"\u8FD9\u9053\u9898\u672A\u56DE\u7B54"}return t!=null&&(i=t.type)!==null&&i!==void 0&&i.startsWith("Matrix")?ye({schema:t,errMsg:"\u95EE\u9898\u672A\u56DE\u7B54\u5B8C\u6574",value:e}):t!=null&&t.parent?S({schema:t,value:e,errMsg:(s==null||(n=s.attribute)===null||n===void 0?void 0:n.validateMsgOptionRequired)||"\u8FD9\u4E2A\u9009\u9879\u672A\u56DE\u7B54",isValid:function(h){var c;return(0,me.Kn)(t)&&t!==null&&t!==void 0&&(c=t.attribute)!==null&&c!==void 0&&c.dataType&&(e[t.id]===!0||(0,O.xb)(e[t.id]))?!1:!(0,O.xb)(h)}}):null},text:function(e,r){var i,n,t,a=r.schema;if(E(e,a))return null;var s=(i=r.schema)===null||i===void 0||(n=i.attribute)===null||n===void 0?void 0:n.regexp;return s==null||(t=s.map(function(l){return S({errMsg:l.message,schema:a,value:e,isValid:function(h){return h!==void 0&&typeof h=="string"?new RegExp(l.expression).test(h):!0}})}).filter(function(l){return!!l}))===null||t===void 0?void 0:t[0]},answerLimit:function(e,r){var i,n=r.schema,t=n==null||(i=n.attribute)===null||i===void 0?void 0:i.answerLimit;if(!t)return null;var a=t.slice(1,t.length-1).split(","),s=parseInt(a[0]||"0"),l=parseInt(a[1]||"0"),d=Object.keys(e||{}).length;return e&&(d<s&&s!==0||d>l&&l!==0)?s===0?"\u6700\u591A\u9009\u62E9".concat(l,"\u9879"):l===0?"\u6700\u5C11\u9009\u62E9".concat(s,"\u9879"):"\u6700\u5C11\u9009\u62E9".concat(s,"\u9879,\u6700\u591A\u9009\u62E9").concat(l,"\u9879"):null},number:function(e,r){var i,n=r.schema;if(E(e,n))return null;var t=n==null||(i=n.attribute)===null||i===void 0?void 0:i.numericScale,a="\u8BF7\u8F93\u5165\u6570\u5B57";return t===0?a="\u8BF7\u8F93\u5165\u6574\u6570":t&&t>0&&(a="\u8BF7\u4FDD\u7559".concat(t,"\u4F4D\u5C0F\u6570")),S({errMsg:a,schema:n,value:e,isValid:function(l){return!(l!==void 0&&(!ie(l)||t===0&&!pe(l)||t!==void 0&&t>0&&("".concat(l).split(".")[1]||"").length>t))}})},date:function(e,r){var i,n=r.schema;if(E(e,n))return null;if(n!=null&&(i=n.attribute)!==null&&i!==void 0&&i.dateTimeFormat&&e&&e[n.id]){var t=ce()(e[n.id],n.attribute.dateTimeFormat).isValid();return t?null:"\u65E5\u671F\u683C\u5F0F\u4E0D\u6B63\u786E"}return N({errMsg:"\u8BF7\u8F93\u5165\u65E5\u671F",schema:n,regEx:/^(?:(?:1[6-9]|[2-9][0-9])[0-9]{2}([-/.]?)(?:(?:0?[1-9]|1[0-2])\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\1(?:29|30)|(?:0?[13578]|1[02])\1(?:31))|(?:(?:1[6-9]|[2-9][0-9])(?:0[48]|[2468][048]|[13579][26])|(?:16|[2468][048]|[3579][26])00)([-/.]?)0?2\2(?:29))(\s+([01][0-9]:|2[0-3]:)?[0-5][0-9]:[0-5][0-9])?$/,value:e})},dateTime:function(e,r){var i=r.schema;return E(e,i)?null:N({errMsg:"\u8BF7\u8F93\u5165\u65E5\u671F\u65F6\u95F4",schema:i,regEx:/^\d{4}[-]([0][1-9]|(1[0-2]))[-]([1-9]|([012]\d)|(3[01]))\s(([0-1]{1}[0-9]{1})|([2]{1}[0-4]{1}))([:])(([0-5]{1}[0-9]{1}|[6]{1}[0]{1}))([:])((([0-5]{1}[0-9]{1}|[6]{1}[0]{1})))$/,value:e})},time:function(e,r){var i=r.schema;return E(e,i)?null:N({errMsg:"\u8BF7\u8F93\u5165\u65F6\u95F4",schema:i,regEx:/^(([0-1]{1}[0-9]{1})|([2]{1}[0-4]{1}))([:])(([0-5]{1}[0-9]{1}|[6]{1}[0]{1}))([:])((([0-5]{1}[0-9]{1}|[6]{1}[0]{1})))$/,value:e})},mobile:function(e,r){var i=r.schema;return E(e,i)||e[i.id]===!0?null:N({errMsg:"\u624B\u673A\u53F7\u683C\u5F0F\u4E0D\u6B63\u786E",schema:i,regEx:/^1[0-9]{10}$/,value:e})},decimal:function(e,r){var i=r.schema;return E(e,i)?null:N({errMsg:"\u8BF7\u8F93\u5165\u6570\u5B57",schema:i,regEx:/^[+-]?\d+(\.\d+)?$/,value:e})},email:function(e,r){var i=r.schema;return E(e,i)?null:N({errMsg:"\u90AE\u4EF6\u683C\u5F0F\u4E0D\u6B63\u786E",schema:i,regEx:/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,value:e})},idCard:function(e,r){var i=r.schema;return E(e,i)?null:S({errMsg:"\u8EAB\u4EFD\u8BC1\u683C\u5F0F\u4E0D\u6B63\u786E",schema:i,value:e,isValid:function(t){return!(t&&!Ve("".concat(t)))}})},chinese:function(e,r){var i=r.schema;return E(e,i)?null:N({errMsg:"\u8BF7\u8F93\u5165\u6C49\u5B57",schema:i,regEx:/^[\u4e00-\u9fa5]+$/,value:e})},alphabet:function(e,r){var i=r.schema;return E(e,i)?null:N({errMsg:"\u8BF7\u8F93\u5165\u5B57\u6BCD",schema:i,regEx:/^[a-zA-Z]+$/,value:e})},textLimit:function(e,r){var i,n=r.schema;if(E(e,n))return null;var t=n==null||(i=n.attribute)===null||i===void 0?void 0:i.textLimit;if(!t)return null;var a=t.slice(1,t.length-1).split(","),s=parseInt(a[0]||"0"),l=parseInt(a[1]||"0");if(s===0&&l===0)return null;var d=S({errMsg:s!==l?"\u8BF7\u8F93\u5165".concat(s,"~").concat(l,"\u4E2A\u5B57"):"\u8BF7\u8F93\u5165".concat(s,"\u4E2A\u5B57"),schema:n,value:e,isValid:function(c){return!(c&&("".concat(c).length<s||"".concat(c).length>l))}});return d},scope:function(e,r){var i,n,t,a=r.schema;if(E(e,a))return null;var s=a==null||(i=a.attribute)===null||i===void 0?void 0:i.scope,l=a==null||(n=a.attribute)===null||n===void 0?void 0:n.dataType;if(s&&l){if(l==="number")return S({errMsg:"\u6570\u503C\u8303\u56F4\u4E0D\u6B63\u786E ".concat(s),schema:a,value:e,isValid:function(f){return!(f!==void 0&&!(0,Q.$l)(s,"".concat(f)))}});if(l==="date")return S({errMsg:"\u65E5\u671F\u8303\u56F4\u4E0D\u6B63\u786E ".concat(s),schema:a,value:e,isValid:function(f){return!(f&&!(0,Q.sm)(s,"".concat(f)))}});if(l==="dateTime")return S({errMsg:"\u65E5\u671F\u65F6\u95F4\u8303\u56F4\u4E0D\u6B63\u786E ".concat(s),schema:a,value:e,isValid:function(f){return!(f&&!(0,Q.de)(s,"".concat(f)))}});if(l==="time")return S({errMsg:"\u65F6\u95F4\u8303\u56F4\u4E0D\u6B63\u786E ".concat(s),schema:a,value:e,isValid:function(f){return!(f&&!(0,Q.YM)(s,"".concat(f)))}})}else if((a==null?void 0:a.type)==="MatrixAuto"&&(t=a.attribute)!==null&&t!==void 0&&t.scope){var d,h=(d=a.attribute)===null||d===void 0?void 0:d.scope,c=(0,Q.$l)(h,"".concat(Object.keys(e).length));if(c)return null;var y=h.substring(1,h.indexOf(",")),v=h.substring(h.indexOf(",")+1,h.length-1),g=[];return y&&g.push("\u6700\u5C11".concat(y,"\u884C")),v&&g.push("\u6700\u591A".concat(v,"\u884C")),g.join("\uFF0C")}return null},horzBlank:function(e,r){return(0,$.Z)((0,Z.Z)().mark(function i(){var n,t,a,s,l,d,h,c;return(0,Z.Z)().wrap(function(v){for(;;)switch(v.prev=v.next){case 0:if(t=r.schema,e){v.next=3;break}return v.abrupt("return",null);case 3:if(!((t==null||(n=t.attribute)===null||n===void 0?void 0:n.dataType)!=="horzBlank"||e[t.id]===void 0||!t.children||!t.parent)){v.next=5;break}return v.abrupt("return",null);case 5:a=null,s=0;case 7:if(!(s<((l=t.children)===null||l===void 0?void 0:l.length))){v.next=17;break}return d=t.children[s],h=(0,ve.j)(d,t),v.next=12,(0,te.Gu)(e[t.id],h,{validateFirst:!0});case 12:c=v.sent,c.error&&c.error.length>0&&(a=c.error[0]);case 14:s++,v.next=7;break;case 17:return v.abrupt("return",a);case 18:case"end":return v.stop()}},i)}))()}};(0,te.au)(xe);var Me=o(8166),ne=o(8373),L=o(85893);window.katex=Me.Z;var re=(0,M.forwardRef)(function(u,e){var r=u.theme,i=r===void 0?K.uH:r,n=u.footerVisible,t=n===void 0?!0:n,a=u.headerVisible,s=a===void 0?!0:a,l=u.loading,d=u.questionNumber,h=d===void 0?!0:d,c=u.onUpload,y=u.progressBar,v=y===void 0?!1:y,g=u.statistics,m=u.isPreview,f=u.mode,b=f===void 0?"survey":f,P=u.correctAnswerVisible,x=P===void 0?!1:P,F=u.examScoreVisible,A=F===void 0?!1:F,B=u.requiredVisible,W=B===void 0?!0:B,J=u.onDictSearch,Pe=u.onLink,Ze=u.triggerType,H=(0,M.useMemo)(function(){return new de(u)},[]),I=H.form,w=(0,M.useMemo)(function(){return(0,O.Gt)(u.schema)},[u.schema]),Fe=(0,M.useMemo)(function(){var C={},D=function U(z){C[z.id]=z.title,z.children&&z.children.forEach(U)};return D(w),document.title=(0,_.WO)(w.title),C},[w]),Ee=(0,M.useMemo)(function(){return(0,O.R6)(u.schema)},[u.schema]);(0,M.useImperativeHandle)(e,function(){return{submit:function(){return H.submit()},reset:function(){I.reset()},setValues:function(D){I.setValues(D,"overwrite")},setValue:function(D,U){I.setFieldState(D,function(z){return z.value=U})},toggleReadOnly:function(D){I.setState(function(U){U.pattern=D?"readPretty":"editable"})},getValues:function(){return I.values},getStore:function(){return H}}}),(0,M.useEffect)(function(){u.onInit&&u.onInit(I)},[]);var Ae=(0,M.useMemo)(function(){return{theme:i,schema:w,footerVisible:t,questionNumber:h,headerVisible:s,optionTypes:Ee,onUpload:c,loading:l,id2title:Fe,progressBar:v,innerStore:H,statistics:g,isPreview:m,mode:b,correctAnswerVisible:x,examScoreVisible:A,onDictSearch:J,requiredVisible:W,onLink:Pe,triggerType:Ze}},[l,w,g]);return(0,L.jsx)(M.Suspense,{fallback:(0,L.jsx)(ne.gb,{}),children:(0,L.jsx)(ne.ZP,{theme:i,children:(0,L.jsx)(R.RV,{form:I,children:(0,L.jsx)(k.Z.Provider,{value:Ae,children:(0,L.jsx)(G.k9,{schema:w})})})})})}),Oe=re},8373:function(ae,X,o){"use strict";o.d(X,{gb:function(){return Y}});var R=o(67294),M=o(85893),G=R.lazy(function(){return Promise.all([o.e(5171),o.e(9223),o.e(8950),o.e(7115),o.e(3074),o.e(701),o.e(3305),o.e(4399),o.e(239),o.e(5613),o.e(8550),o.e(4839),o.e(5206),o.e(3524),o.e(849),o.e(8867),o.e(1044),o.e(371),o.e(1271)]).then(o.bind(o,95771))}),k=R.lazy(function(){return Promise.all([o.e(5171),o.e(9223),o.e(8950),o.e(4257),o.e(7115),o.e(3074),o.e(5843),o.e(701),o.e(3305),o.e(4399),o.e(5613),o.e(8550),o.e(4839),o.e(5206),o.e(2684),o.e(8867),o.e(1031),o.e(1044),o.e(5761),o.e(8851),o.e(5082)]).then(o.bind(o,68209))}),K=R.lazy(function(){return Promise.all([o.e(9223),o.e(7115),o.e(3074),o.e(5843),o.e(239),o.e(4839),o.e(1044),o.e(5524),o.e(2944)]).then(o.bind(o,17707))}),Y=function(){return(0,M.jsx)("div",{className:"page-loading-warp",children:(0,M.jsxs)("div",{className:"spinner",children:[(0,M.jsx)("div",{className:"rect1"}),(0,M.jsx)("div",{className:"rect2"}),(0,M.jsx)("div",{className:"rect3"}),(0,M.jsx)("div",{className:"rect4"}),(0,M.jsx)("div",{className:"rect5"})]})})},q=function(j){var $=j.theme,V=j.children;return $==="antdMobile"?(0,M.jsx)(k,{children:V}):$==="antdForm"?(0,M.jsx)(K,{children:V}):(0,M.jsx)(G,{children:V})};X.ZP=q}}]);
