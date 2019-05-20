import React, {Component} from 'react'
import {Dimensions, Image, NativeModules, ScrollView, StyleSheet, Text, View} from 'react-native'

export default class questionDetail extends Component {

    constructor(props) {
        super(props)

        this.state = {
            question: this.props.navigation.getParam("question"),
            questionInfo: null,
        }
        this.getQuestion()
    }

    getQuestion = () => {
        var questiontitle = this.state.question.originQuestion.stat.question__title_slug
        NativeModules.LeetCodeMudule.getQuestion(questiontitle, (data) => {
            var temp = JSON.parse(data).data.question;
            this.setState({
                questionInfo: temp
            })
            console.log(temp)
        }, (msg) => {
            alert(msg)
        })
    }

    imagesContent = () => {
        // return(<Text>{JSON.stringify(this.state.questionInfo.imgs)}</Text>)

        const imgs = this.state.questionInfo.imgs

        if(imgs === undefined){
            return (<View></View>)
        }

        return (imgs.map((data, index) => {
            return (
                <View key={index} style={styles.imagesContainer}>
                    <Image resizeMode={'contain'} style={styles.img} source={{uri: data}}></Image>
                </View>
            )
        }))

    }

    questionInfoView = () => {
        if (this.state.questionInfo !== null)
        // if (false)
            return (
                <ScrollView>
                    <View style={styles.questionBody}>
                        <Text style={styles.questionBodyTxt}>{this.state.questionInfo.translatedContent}</Text>
                        {this.imagesContent()}
                    </View>
                </ScrollView>
            )
        else
            return (
                <View style={styles.loaddingView}>
                    <Image style={styles.loaddingImage} source={require('../image/loading.png')}></Image>
                    <Text style={styles.loaddingText}>加载中，请稍候....</Text>
                </View>
            )
    }

    render() {
        return (<ScrollView style={styles.container}>
            <View style={styles.titleContainer}>
                <Text style={styles.title}>{this.state.question.title} --- {this.state.question.question.questionId}</Text>
            </View>
            {this.questionInfoView()}
        </ScrollView>)
    }

}

const styles = StyleSheet.create({
    title: {
        fontSize: 18,
        marginLeft: 8
    },
    titleContainer: {
        marginTop: 10,
        padding: 5,
        backgroundColor: '#f1f1f1',
        width: Dimensions.get('window').width - 20,
        marginLeft: 10,
        borderWidth: 2,
        borderColor: '#cccccc',
        borderRadius: 5,
        fontWeight: 'bold'
    },
    container: {},
    loaddingView: {
        width: Dimensions.get('window').width,
        justifyContent: 'center',
        alignItems: 'center',
        top: 0,
        left: 0,
        height: Dimensions.get('window').width / 7 + 300,
    },
    loaddingImage: {
        width: Dimensions.get('window').width / 7,
        height: Dimensions.get('window').width / 7
    },
    loaddingText: {
        marginTop: 15,
        fontSize: 15
    },
    questionBody: {
        marginTop: 10,
        padding: 5,
        backgroundColor: '#f1f1f1',
        width: Dimensions.get('window').width - 20,
        marginLeft: 10,
        borderWidth: 2,
        borderColor: '#cccccc',
        borderRadius: 5,
        fontWeight: 'bold',
        marginBottom: 30
    },
    questionBodyTxt: {
        fontSize: 16
    },
    imagesContainer: {
        width: Dimensions.get('window').width - 30,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        marginBottom: 10
    },
    img: {
        width: Dimensions.get('window').width - 30,
        height: Dimensions.get('window').width * 0.7,
    }

})
