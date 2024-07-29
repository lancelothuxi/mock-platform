<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="是否启用" prop="enabled">
        <el-select v-model="queryParams.enabled" placeholder="请选择是否启用" clearable>
          <el-option
            v-for="dict in sys_yes_no"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="匹配规则" prop="matchType">
        <el-select v-model="queryParams.matchType" placeholder="请选择匹配规则" clearable>
          <el-option
            v-for="dict in mock_expression_rule_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['mock:data:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['mock:data:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['mock:data:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['mock:data:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="mock数据" align="center" prop="data" />
      <el-table-column label="超时时间" align="center" prop="timeout" />
      <el-table-column label="是否启用" align="center" prop="enabled">
        <template #default="scope">
          <dict-tag :options="sys_yes_no" :value="scope.row.enabled"/>
        </template>
      </el-table-column>
      <el-table-column label="匹配规则" align="center" prop="matchType">
        <template #default="scope">
          <dict-tag :options="mock_expression_rule_type" :value="scope.row.matchType"/>
        </template>
      </el-table-column>
      <el-table-column label="匹配规则" align="center" prop="matchExpression" />
      <el-table-column label="创建时间" align="center" prop="createdTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createdTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mock:data:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mock:data:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改mock数据对话框 -->
    <el-dialog :title="title" v-model="open" width="700px" append-to-body>
      <el-form ref="dataRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="mock数据" style="height: 300px">
          <IEditor v-model="form.data"/>
        </el-form-item>
        <el-form-item label="超时时间" prop="timeout">
          <el-input v-model="form.timeout" placeholder="输入超时时间" />
        </el-form-item>
        <el-form-item label="是否启用" prop="enabled">
          <el-select v-model="form.enabled" placeholder="是否启用">
            <el-option
              v-for="dict in sys_yes_no"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="规则类型" prop="matchType">
          <el-select v-model="form.matchType" placeholder="规则类型">
            <el-option
              v-for="dict in mock_expression_rule_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="匹配规则" prop="matchExpression">
          <IEditor v-model:content="form.      "/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Data">
import { listData, getData, delData, addData, updateData } from "@/api/mock/data";
import IEditor from "@/views/mock/data/IEditor.vue";
const { proxy } = getCurrentInstance();
const { sys_yes_no, mock_expression_rule_type } = proxy.useDict('sys_yes_no', 'mock_expression_rule_type');

const dataList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const route = useRoute();
const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    enabled: null,
    matchType: null,
    matchExpression: null,
  },
  rules: {
    enabled: [
      { required: true, message: "是否启用不能为空", trigger: "change" }
    ],
    mockConfigId: [
      { required: true, message: "mock规则配置表的id不能为空", trigger: "blur" }
    ]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询mock数据列表 */
function getList(query) {
  loading.value = true;
  listData(query).then(response => {
    dataList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    data: null,
    timeout: null,
    enabled: null,
    mockConfigId: null,
    matchType: null,
    matchExpression: null,
    createdTime: null,
    updatedTime: null
  };
  proxy.resetForm("dataRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加mock数据";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getData(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改mock数据";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["dataRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateData(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addData(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除mock数据编号为"' + _ids + '"的数据项？').then(function() {
    return delData(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('mock/data/export', {
    ...queryParams.value
  }, `data_${new Date().getTime()}.xlsx`)
}

getList(route.query);
</script>
