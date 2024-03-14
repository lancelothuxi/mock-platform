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
  mounted() {
    ace.config.set("basePath", "https://cdnjs.cloudflare.com/ajax/libs/ace/1.4.14/");
    //快速开始-demo
    this.editor = ace.edit(this.$refs.ace, {
      maxLines: 24, // 最大行数，超过会自动出现滚动条
      minLines: 12, // 最小行数，还未到最大行数时，编辑器会自动伸缩大小
      fontSize: 10, // 编辑器内字体大小
      theme: 'ace/theme/github', // 默认设置的主题
      mode: 'ace/mode/json', // 默认设置的语言模式
      tabSize: 4,// 制表符设置为 4 个空格大小
      readOnly: false //只读
    });
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
