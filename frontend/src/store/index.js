import {createStore} from 'vuex'

export default createStore({
    state: {
        messages: {
            currentConvShown: {
                type: String,
            }
        },
        user: {},
        friends: [],
        groups: {},
        night: {
            color1: "#13111A",
            color2: "#454150",
            color3: "#909090",
            color4: "#E85C5C",
            color5: "#F4F4F4",
            color6: "#FFFFFF"
        },
    },
    mutations: {
        SET_CONV_UUID(state, uuid) {
            state.messages.currentConvShown = uuid;
        },
        SET_USER(state, user) {
            state.user = user;
        },
        ADD_FRIEND(state, user) {
            state.friends.push(user);
        },
        SET_GROUP(state, groups) {
            state.groups = groups;
        }
    },
    actions: {
        setConvUUID(context, uuid) {
            context.commit('SET_CONV_UUID', uuid);
        },
        setUser(context, user) {
            context.commit('SET_USER', user);
        },
        addUser(context, user) {
            context.commit('ADD_FRIEND', user);
        },
        setGroup(context, groups) {
            context.commit('SET_GROUP', groups);
        }
    },
    getters: {
        getColors: state => {
            return state.night;
        },
        getCurrentConv: state => {
            return state.messages.currentConvShown;
        },
        getUser: state => {
            return state.user;
        },
        getFriends: state => {
            return state.friends;
        },
        getGroup: state => {
            return state.groups;
        }
    },
    modules: {}
})
