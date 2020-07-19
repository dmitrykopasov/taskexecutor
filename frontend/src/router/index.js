import Vue from 'vue'
import VueRouter from 'vue-router'
import Main from '../components/Main.vue'
import TaskEditor from '../components/TaskEditor.vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'main',
        component: Main
    },
    {
        path: '/create',
        name: 'createTask',
        component: TaskEditor
    },
    {
        path: '/edit',
        name: 'taskEditor',
        component: TaskEditor,
        props: true
    }
]

const router = new VueRouter({
    routes
})

export default router
