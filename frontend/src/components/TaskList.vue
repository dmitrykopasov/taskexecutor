<template>
    <div>
        <b-table striped hover bordered caption-top
                 :label="'Список задач'"
                 :items="items"
                 :fields="table.fields"
                 @row-dblclicked="openTask">
            <template v-slot:table-caption><h5>Список задач</h5></template>
        </b-table>
    </div>
</template>

<script>

    export default {
        name: 'TaskList',
        props: ['items'],
        data() {
            return {
                table: {
                    fields: [
                        {key: 'state', label: 'Статус', formatter: (value) => this.formatterState(value)},
                        {key: 'priority', label: 'Приоритет'},
                        {key: 'path', label: 'Путь к файлу'},
                        {key: 'cpu', label: 'ЦПУ, %'},
                        {key: 'memory', label: 'Память, %'},
                        {key: 'execTime', label: 'Время выполнения, сек'},
                        {key: 'createdAt', label: 'Дата создания'}
                    ]
                }
            }
        },
        methods: {
            openTask(row) {
                this.$router.push({name: 'taskEditor', params: {taskId: row.id}})
            },
            formatterState(value) {
                if (value == 'IN_PROGRESS') {
                    return 'В работе'
                } else if (value == 'WAITING') {
                    return 'Ожидает'
                }
            }
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
