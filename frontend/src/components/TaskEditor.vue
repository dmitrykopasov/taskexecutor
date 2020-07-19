<template>
    <div class="container">
        <b-form>
            <h5 v-if="isCreateTask">Создание новой задачи</h5>
            <h5 v-else>Просмотр</h5>

            <div class="row" v-if="!isCreateTask">
                <div class="col-lg-2"></div>
                <div class="col-lg-2">
                    <label>Статус</label>
                </div>
                <div class="col-lg-6">
                    <b-input class="form-control" :readonly="!isCreateTask" v-model="formatterState"/>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-2"></div>
                <div class="col-lg-2">
                    <label>Приоритет</label>
                </div>
                <div class="col-lg-6">
                    <b-input class="form-control" :readonly="!isCreateTask" type="number" v-model="task.priority"/>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-2"></div>
                <div class="col-lg-2">
                    <label>Путь к файлу</label>
                </div>
                <div class="col-lg-6">
                    <b-input class="form-control" :readonly="!isCreateTask" v-model="task.path"/>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-2"></div>
                <div class="col-lg-2">
                    <label>ЦПУ</label>
                </div>
                <div class="col-lg-6">
                    <b-input class="form-control" :readonly="!isCreateTask" v-model="task.cpu"/>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-2"></div>
                <div class="col-lg-2">
                    <label>Память, %</label>
                </div>
                <div class="col-lg-6">
                    <b-input class="form-control" :readonly="!isCreateTask" type="number" v-model="task.memory"/>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-2"></div>
                <div class="col-lg-2">
                    <label>Время выполнения</label>
                </div>
                <div class="col-lg-6">
                    <b-input class="form-control" :readonly="!isCreateTask" type="number" v-model="task.execTime"/>
                </div>
            </div>

            <div class="row" v-if="!isCreateTask">
                <div class="col-lg-2"></div>
                <div class="col-lg-2">
                    <label>Время создания</label>
                </div>
                <div class="col-lg-6">
                    <b-input class="form-control" :readonly="!isCreateTask" v-model="task.createdAt"/>
                </div>
            </div>

            <div class="b-table-top-row">
                <b-button v-if="isCreateTask" variant="primary"
                          @click="saveTask">
                    Сохранить
                </b-button>
                <b-button v-if="canRemove" variant="danger"
                          @click="removeTask">
                    Удалить
                </b-button>
                <b-button variant="danger"
                          @click="cancel">
                    Отмена
                </b-button>
            </div>
        </b-form>
    </div>
</template>

<script>
    import Task from "../domain/Task";

    export default {
        name: "TaskEditor",
        props: ['taskId'],
        data() {
            return {
                task: new Task()
            }
        },
        computed: {
            isCreateTask() {
                return !this.taskId;
            },
            canRemove() {
                if (this.isCreateTask || this.task.state == 'IN_PROGRESS') {
                    return false
                } else {
                    return true
                }
            },
            formatterState() {
                if (this.task.state == 'IN_PROGRESS') {
                    return 'В работе'
                } else if (this.task.state == 'WAITING') {
                    return 'Ожидает'
                }
                return ''
            }
        },
        created() {
            if (!this.isCreateTask) {
                this.task = this.$store.getters.getTaskById(this.taskId)
            }
        },
        methods: {
            async saveTask() {
                await this.$store.dispatch('createTask', this.task)
                this.$router.push({name: 'main'})
            },
            async removeTask() {
                await this.$store.dispatch('removeTask', this.task.id)
                this.$router.push({name: 'main'})
            },
            cancel() {
                this.$router.push({name: 'main'})
            }
        }
    }
</script>

<style scoped>
    .row {
        margin-top: 20px;
    }

    .btn {
        margin: 5px;
    }

    .b-table-top-row {
        margin-top: 20px;
    }
</style>