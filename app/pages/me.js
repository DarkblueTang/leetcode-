import React, {Component} from 'react'
import {FlatList, NativeModules, ToastAndroid, TouchableHighlight, View} from 'react-native'
import {ListItem} from 'react-native-elements'

export default class me extends Component {

    constructor(props) {
        super(props)

        this.state = {
            listitem: []
        }


        this.getUserInfo()
    }

    getUserInfo = () => {
        NativeModules.LeetCodeMudule.getUserInfo((data) => {
            data = JSON.parse(data)

            this.parseUserInfo(data)

        }, (msg) => {
            alert(msg)
        })
    }

    parseUserInfo = (data) => {
        var temp = [
            {
                title: '我已经做了： ' + data.solvedTotal,
                img: require('../image/ok.png')
            },
            {
                title: '已经尝试做的题目： ' + data.attempted,
                img: require('../image/attempt.png')
            },
            {
                title: '我还没有做的题目： ' + (data.unsolved - data.attempted),
                img: require('../image/notdo.png')
            },
            {
                title: '题目总数量： ' + (data.questionTotal),
                img: require('../image/Qcount.png')
            },
            {
                title: '已做简单题目数量： ' + (data.solvedPerDifficulty.Easy),
                img: require('../image/done.png')
            },
            {
                title: '已做中等题目数量： ' + (data.solvedPerDifficulty.Medium),
                img: require('../image/done.png')
            },
            {
                title: '已做复杂题目数量： ' + (data.solvedPerDifficulty.Hard),
                img: require('../image/done.png')
            },
            {
                title: '我的经验： ' + (data.XP),
                img: require('../image/experiences.png')
            },
            {
                title: '我的金币： ' + (data.leetCoins),
                img: require('../image/money.png')
            },
            {
                title: '退出登录',
                img: require('../image/exit.png')
            },
        ]
        this.setState({
            listitem: temp
        })
    }

    press = (index) => {
        if (index === 9) {

            NativeModules.LeetCodeMudule.cleanUserInfo(()=>{
                ToastAndroid.show("清除用户信息成功", 1000)
                this.props.navigation.replace("login")
            }, ()=>{
                ToastAndroid.show("清除用户信息失败", 1000)
                this.props.navigation.replace("login")
            })

        }
    }


    renderItem = ({item, index}) => (
        <TouchableHighlight onPress={() => {
            this.press(index)
        }}>
            <ListItem
                title={item.title}
                leftAvatar={{source: item.img}}
            />
        </TouchableHighlight>
    )

    render() {
        return (
            <View>
                <FlatList
                    data={this.state.listitem}
                    renderItem={this.renderItem}
                ></FlatList>
            </View>
        )
    }
}
