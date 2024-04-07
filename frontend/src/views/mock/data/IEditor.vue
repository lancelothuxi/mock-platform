<template>
    <div class="ace">
      <p>快速开始-demo</p>
      <div ref="aceRef" class="ace-editor"></div>
    </div>
</template>
<script setup>
import {ref, onMounted, defineProps, watch} from 'vue';
import ace from 'ace-builds';
import 'ace-builds/src-min-noconflict/theme-kuroir';
import 'ace-builds/src-min-noconflict/ext-searchbox';
import 'ace-builds/src-min-noconflict/mode-javascript';
import 'ace-builds/src-min-noconflict/ext-language_tools';

const aceRef = ref(null);
const editor = ref(null);

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  }
});

onMounted(() => {
  editor.value = ace.edit(aceRef.value, {
    useWorker: false,
    maxLines: 24,
    minLines: 12,
    fontSize: 14,
    theme: 'ace/theme/kuroir',
    mode: 'ace/mode/javascript',
    tabSize: 4,
    readOnly: false
  });

  editor.value.getSession().setValue(props.modelValue);

});

watch(() => props.modelValue, (newValue) => {
  if (newValue !== editor.value.getSession().getValue()) {
    editor.value.getSession().setValue(newValue);
  }
});
</script>

<style scoped>
.ace-editor {
  margin-bottom: 20px;
}

.ace-toolbar {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.ace-toolbar > button {
  margin-left: 20px;
}
</style>
