---
id: calculate
title: 计算逻辑
---
:::important 什么是计算逻辑？
当前问题的答案可以通过别的问题的答案来动态计算。

打开选项设置栏，点击**公式计算**。
:::

## 普通计算逻辑

某个问题的答案会根据其他问题的答案动态计算。

### 例子

总收入必须等于收入明细，否则问卷不能提交，并在问题上提示错误消息。

:::note 问卷
Q1. 身高(cm)【填空】

Q2. 体重(kg)【填空】

Q3. BMI值【填空】
:::

### 自定义公式

:::tip 公式
BMI= **体重**/(**身高**/100 * **身高**/100)

公式解析：计算公式 BMI=体重(以千克为单位)除以身高的平方(以米为单位)。
:::

<details>
<summary>如何操作</summary>

![logic-validate](../../../static/img/logic-calculate.gif)

</details>

### 在线效果预览

<div>
  <iframe src="https://wj.surveyking.cn/s/j02tex?preview=1" style={{border: "1px solid #eee", marginBottom: "1em"}} width="100%" height="400" />
</div>
