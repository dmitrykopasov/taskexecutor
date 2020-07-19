export default class Task {

    constructor() {
        this.id = null
        this.state = null
        this.priority = null
        this.cpu = null
        this.memory = null
        this.execTime = null
        this.createdAt = null
        this.path = ''
    }

    static createNewFromJson(json) {
        let task = new Task()
        for (let prop of Object.keys(json)) {
            if (prop == 'createdAt') {
                task[prop] = new Date(json[prop]).toUTCString()
            } else {
                task[prop] = json[prop]
            }
        }
        return task
    }
}