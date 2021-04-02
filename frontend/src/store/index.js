import {createStore} from 'vuex'

export default createStore({
    state: {
        theme: {
            night: {
                color1: "#13111A",
                color2: "#454150",
                color3: "#909090",
                color4: "#E85C5C",
                color5: "#F4F4F4",
                color6: "#FFFFFF"
            },
            day: {
                color1: "#FDFDFD",
                color2: "#454150",
                color3: "#909090",
                color4: "#E85C5C",
                color5: "#454150",
                color6: "#FFFFFF"
            },
            currentMode: {
                type: Boolean,
                default: true
                // Represents the mode. If currentMode is true, then night mode is enabled, else it's day mode.
            }
        },
        messages: {
            currentConvShown: {
                type: String,
            }
        },
        user: {  },
        friends: [

        ]
    },
    mutations: {
        SWITCH_THEME(state) {
            state.theme.currentMode = !state.theme.currentMode
        },
        SET_CONV_UUID(state, uuid) {
            state.messages.currentConvShown = uuid;
        },
        SET_USER(state, user) {
            state.user = user;
        },
        ADD_FRIEND(state, user) {
            state.friends.push(user);
        }
    },
    actions: {
        switchTheme(context) {
            context.commit('SWITCH_THEME');
        },
        setConvUUID(context, uuid) {
            context.commit('SET_CONV_UUID', uuid);
        },
        setUser(context, user) {
            context.commit('SET_USER', user);
        },
        addUser(context, user) {
            context.commit('ADD_FRIEND', user);
        }
    },
    getters: {
        getColors: state => {
            if (state.theme.currentMode)
                return state.theme.night;
            else
                return state.theme.day;
        },
        getTheme: state => {
            return state.theme.currentMode;
        },
        getCurrentConv: state => {
            return state.messages.currentConvShown;
        },
        getUser: state => {
            return state.user;
        },
        getFriends: state => {
            return state.friends;
        }
    },
    modules: {

    }
})
