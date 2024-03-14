<template>
  <div ref="ace" class="ace-editor"/>
</template>
<script>
//ace-editor
import ace from 'ace-builds'
// ace主题包
import 'ace-builds/src-min-noconflict/theme-kuroir'
import 'ace-builds/src-min-noconflict/theme-one_dark'
import 'ace-builds/src-min-noconflict/theme-github'
// ace语言包
import 'ace-builds/src-min-noconflict/mode-javascript'
import 'ace-builds/src-min-noconflict/mode-json5'
//代码完成
import 'ace-builds/src-min-noconflict/ext-language_tools'
export default {
  data() {
    return {
      editor: null
    }
  },
  props: {
    value: {
      type: String,
      default: '',
    },
  },

  setup(props, vm) {
    let options = {
      theme: 'ace/theme/tomorrow', // 主题
      mode: 'ace/mode/mysql', // 代码匹配模式
      tabSize: 2, //标签大小
      fontSize: 14, //设置字号
      wrap: true, // 用户输入的sql语句，自动换行
      enableSnippets: true, // 启用代码段
      showLineNumbers: true, // 显示行号
      enableLiveAutocompletion: true, // 启用实时自动完成功能 （比如：智能代码提示）
      enableBasicAutocompletion: true, // 启用基本自动完成功能
      scrollPastEnd: true, // 滚动位置
      highlightActiveLine: true, // 高亮当前行
    }
  },
  mounted() {
    this.editor = ace.edit(this.$refs.ace, this.options);
    this.editor.setValue('dddds') // 设置内容
  },
  methods: {
  }
}
</script>

<style scoped>
.ace-editor {
  position: static;
  width: 1000px;
  height: 200px;
}
</style>
