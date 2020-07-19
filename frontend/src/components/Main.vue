<template>
    <div class="container">
        <div class="b-table-top-row">
            <b-button variant="primary"
                      @click="reload">
                Обновить
            </b-button>
            <b-button variant="success"
                      @click="createTask">
                Создать задачу
            </b-button>
        </div>
        <div class="b-table-top-row">
            <TaskList :items="getTasks"/>
        </div>
    </div>
</template>

<script>
    import TaskList from '@/components/TaskList.vue'

    export default {
        name: 'Main',
        data() {
            return {}
        },
        computed: {
            getTasks() {
                return this.$store.getters.getTasks
            }
        },
        methods: {
            createTask() {
                this.$router.push({name: 'createTask'})
            },
            async reload () {
                await this.$store.dispatch('loadTasks')
            }
        },
        components: {
            TaskList
        }
    }
</script>

<style scoped>
    .btn {
        margin: 5px;
    }
</style>
