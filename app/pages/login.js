import React, {Component} from 'react'
import {Dimensions, Image, NativeModules, StyleSheet, Text, ToastAndroid, View} from 'react-native'
import {Button, Input} from 'react-native-elements';

export default class login extends Component {
    constructor(props) {
        super(props)

        this.state = {
            loginButtonEnable: true,
            loginButtonText: '登录',
            username: '',
            password: ''
        }


        NativeModules.LeetCodeMudule.checkLogin(() => {
            ToastAndroid.show("读取用户信息成功", 1000)
            this.setState({loginButtonEnable: false, loginButtonText: '即将跳转...'})
            this.props.navigation.replace("main")
        }, (msg) => {
            if (msg !== null && msg !== undefined && msg !== '')
                alert(msg)
            else {
                ToastAndroid.show("请输入用户名和密码登录", 1000)
            }
        })
    }

    toLogin = () => {
        this.setState({loginButtonEnable: false, loginButtonText: '正在登录'})
        NativeModules.LeetCodeMudule.login(this.state.username, this.state.password, () => {
            ToastAndroid.show("登录成功", 1000)
            this.setState({loginButtonEnable: false, loginButtonText: '即将跳转...'})
            this.props.navigation.replace("main")
        }, (msg) => {
            this.setState({loginButtonEnable: true, loginButtonText: '重新登录'})
            ToastAndroid.show(msg, 1000)
        })
    }

    render() {
        return (
            <View style={styles.container}>
                <Text style={styles.header}>登 录</Text>
                <View style={styles.form}>
                    <Input
                        inputStyle={styles.input}
                        leftIcon={
                            <Image style={styles.icon} source={require('../image/user.png')}/>
                        }
                        errorStyle={{color: 'red'}}
                        placeholder={'请输入leetcode用户名'}
                        containerStyle={styles.inputContainer}
                        inputContainerStyle={styles.inputContainerStyle}
                        onChangeText={(username) => this.setState({username})}
                    />
                    <Input
                        inputStyle={styles.input}
                        leftIcon={
                            <Image style={styles.icon} source={require('../image/password.png')}/>
                        }
                        errorStyle={{color: 'red'}}
                        placeholder={'请输入leetcode密码'}
                        containerStyle={styles.inputContainer}
                        inputContainerStyle={styles.inputContainerStyle}
                        onChangeText={(password) => this.setState({password})}
                    />
                </View>
                <View style={styles.buttonStyle}>
                    <Button
                        disabled={!(this.state.username !== '' && this.state.password !== '' && this.state.loginButtonEnable)}
                        onPress={this.toLogin}
                        title={this.state.loginButtonText}></Button>
                </View>
            </View>
        )
    }
}

const styles = StyleSheet.create({
    header: {
        marginTop: -50,
        fontSize: 30,
        marginBottom: 20,
        color: '#cccccc',
        alignItems: "center"
    },
    container: {
        backgroundColor: "#2e3246",
        height: Dimensions.get("window").height + 30,
        width: Dimensions.get("window").width,
        alignItems: "center",
        justifyContent: "center",
    },
    form: {
        width: "98%",
    },
    icon: {
        width: 25,
        height: 25
    },
    input: {
        color: '#6a74a0',
    },
    inputContainer: {
        borderWidth: 2,
        borderColor: '#6a74a0',
        borderRadius: 10,
        marginBottom: 25
    },
    inputContainerStyle: {
        borderBottomWidth: 0
    },
    buttonStyle: {
        width: Dimensions.get("window").width * 0.95,
        marginTop: 50
    }
})
