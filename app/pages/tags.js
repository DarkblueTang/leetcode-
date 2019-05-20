import React, {Component} from 'react'
import {NativeModules, Text, View, FlatList, StyleSheet, Dimensions, Image} from 'react-native'
import {Button} from 'react-native-elements'

export default class tags extends Component {

    constructor(props) {
        super(props)

        this.state = {
            categories: []
        }

        this.getAllCategories()
    }

    getAllCategories = () => {
        NativeModules.LeetCodeMudule.getAllCategories((data) => {
            this.setState({
                categories: JSON.parse(data)
            })
        }, (msg) => {
            alert(msg)
        })
    }

    tagPress = (item)=>{
        console.log(item)
        this.props.navigation.push("tagQuestionList", {tag: item})
    }

    renderItem = ({item, index}) => {

        return (
            <View style={styles.buttonContainer}>
                <View style={styles.buttonView}>
                    <Button
                        onPress={()=>{this.tagPress(item)}}
                        title={item.titleZN + "  --  " + item.count + '  题'}
                        type="outline"
                    />
                </View>
            </View>
        )
    }

    ListFooterComponent = ()=>{
        return(
            <View style={styles.listFooterComponent}></View>
        )
    }

    ListEmptyComponent = ()=>{
        return(
            <View style={styles.loaddingView}>
                <Image style={styles.loaddingImage} source={require('../image/loading.png')}></Image>
                <Text style={styles.loaddingText}>标签正在加载中，请稍候....</Text>
            </View>
        )
    }

    render() {
        return (
            <View>
                <FlatList
                    data={this.state.categories}
                    renderItem={this.renderItem}
                    ListFooterComponent={this.ListFooterComponent}
                    showsHorizontalScrollIndicator={false}
                    showsVerticalScrollIndicator={false}
                    ListEmptyComponent={this.ListEmptyComponent}
                >
                </FlatList>
            </View>
        )
    }
}

const styles = StyleSheet.create({
    buttonContainer: {
        width: Dimensions.get("window").width,
        alignItems: 'center',
        justifyContent: 'center',
        marginTop: 5
    },
    buttonView: {
        width: Dimensions.get("window").width - 30,
    },
    listFooterComponent: {
        marginBottom: 10
    },
    loaddingView: {
        width: Dimensions.get('window').width,
        height: Dimensions.get('window').height,
        justifyContent: 'center',
        alignItems: 'center'
    },
    loaddingImage: {
        width: Dimensions.get('window').width / 7,
        height: Dimensions.get('window').width / 7
    },
    loaddingText: {
        marginTop: 15,
        fontSize: 15
    }
})
