import Vue from 'vue'
import Vuex from 'vuex'
import axios from "axios";
import Task from "../domain/Task";

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        tasks: []
    },
    getters: {
        getTasks: state => {
            return state.tasks
        },
        getTaskById: (state) => id => {
            return state.tasks.find(t => t.id === id)
        }
    },
    mutations: {
        ADD_TASKS(state, tasks) {
            state.tasks = state.tasks.concat(tasks)
        },
        CLEAR_TASKS(state) {
            state.tasks = []
        },
    },
    actions: {
        async loadTasks({commit}) {
            commit('CLEAR_TASKS')
            let response = await axios.get(`/web/tasks`)
            let tasksJson = response.data
            let tasksObj = []
            for (let i in tasksJson) {
                tasksObj.push(Task.createNewFromJson(tasksJson[i]))
            }
            commit('ADD_TASKS', tasksObj)
        },
        async createTask({dispatch}, task) {
            await axios.post(`/web/tasks`, task)
            await dispatch("loadTasks")
        },
        async removeTask({dispatch}, taskId) {
            await axios.delete(`/web/tasks/` + taskId)
            await dispatch("loadTasks")
        }
    }
})
