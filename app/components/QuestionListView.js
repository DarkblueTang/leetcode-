import React, {Component} from 'react'
import {View, Text, FlatList, NativeModules, TouchableHighlight, StyleSheet, Dimensions, Image} from 'react-native'
import {Button} from 'react-native-elements';

export default class QuestionListView extends Component {

    constructor(props) {
        super(props)
        this.state = {
            questionList: [],
            pageIndex: 0,
            pageSize: 30,
            questionShowList: [],
            sortType: 1,
            footHeight: 0
        }
    }

    setQuestionList = (list) => {
        this.setState({
            questionList: list
        })
        this.getQuestionShowList()
    }

    getQuestionShowList = () => {
        var start = this.state.pageIndex * this.state.pageSize;
        var end = (this.state.pageIndex + 1) * this.state.pageSize;
        end = (end > this.state.questionList.length - 1) ? this.state.questionList.length - 1 : end
        var temp = this.state.questionList.slice(start, end + 1)
        this.setState({
            questionShowList: temp
        })
    }

    onEndReached = (distanceFromEnd) => {
    }

    prePage = () => {
        if (this.state.pageIndex > 0) {
            this.state.pageIndex--
            this.getQuestionShowList()
        }
    }
    nextPage = () => {
        var end = (this.state.pageIndex + 1) * this.state.pageSize;
        end = (end > this.state.questionList.length - 1) ? this.state.questionList.length - 1 : end
        if (end < this.state.questionList.length - 1) {
            this.state.pageIndex++
            this.getQuestionShowList()
        }
    }

    ListEmptyComponent = () => {
        return (
            <View style={styles.loaddingView}>
                <Image style={styles.loaddingImage} source={require('../image/loading.png')}></Image>
                <Text style={styles.loaddingText}>题目正在加载中，请稍候....</Text>
            </View>
        )
    }

    getSortFunction = () => {
        switch (this.state.sortType) {
            case 0:
                return function (a, b) {
                    return parseInt(a.question.questionId) - parseInt(b.question.questionId)
                };
            case 1:
                return function (a, b) {
                    return parseInt(b.question.questionId) - parseInt(a.question.questionId)
                };
            case 2:
                return function (a, b) {
                    return parseInt(a.originQuestion.difficulty.level) - parseInt(b.originQuestion.difficulty.level) || parseInt(a.question.questionId) - parseInt(b.question.questionId)
                };
            case 3:
                return function (a, b) {
                    return parseInt(b.originQuestion.difficulty.level) - parseInt(a.originQuestion.difficulty.level) || parseInt(a.question.questionId) - parseInt(b.question.questionId)
                };
            default:
                return function (a, b) {
                    return a.question.questionId > b.question.questionId
                };
        }
    }

    idSort = () => {
        if (this.state.sortType === 0) {
            this.state.sortType = 1
        } else if (this.state.sortType === 1) {
            this.state.sortType = 0
        } else this.state.sortType = 1
        this.state.questionList.sort(this.getSortFunction())
        this.state.pageIndex = 0
        this.getQuestionShowList()
    }

    nanduSort = () => {
        if (this.state.sortType === 2) {
            this.state.sortType = 3
        } else if (this.state.sortType === 3) {
            this.state.sortType = 2
        } else this.state.sortType = 2
        this.state.questionList.sort(this.getSortFunction())
        this.state.pageIndex = 0
        this.getQuestionShowList()
    }

    idSortIcon = () => {
        if (this.state.sortType === 0) {
            return (<Image style={styles.sortIcon} source={require('../image/up.png')}></Image>)
        } else if (this.state.sortType === 1) {
            return (<Image style={styles.sortIcon} source={require('../image/down.png')}></Image>)
        } else {
            return (<View></View>)
        }
    }

    nanduSortIcon = () => {
        if (this.state.sortType === 2) {
            return (<Image style={styles.sortIcon} source={require('../image/up.png')}></Image>)
        } else if (this.state.sortType === 3) {
            return (<Image style={styles.sortIcon} source={require('../image/down.png')}></Image>)
        } else {
            return (<View></View>)
        }
    }

    showDetail = (item) => {
        this.props.navigation.push('questionDetail', {question: item})
    }

    ListHeaderComponent = () => {
        if (this.state.questionShowList.length > 0)
            return (
                <View style={styles.listHeader}>
                    <View style={styles.listHeaderItem}><Text style={styles.listHeaderText}>排序方式：</Text></View>
                    <View style={styles.listHeaderItem}>
                        <Button onPress={this.idSort} title={'id'}
                                icon={this.idSortIcon}
                                iconRight={true}
                        ></Button>
                    </View>
                    <View style={styles.listHeaderItem}>
                        <Button onPress={this.nanduSort} title={'难度'}
                                icon={this.nanduSortIcon}
                                iconRight={true}
                        ></Button>
                    </View>
                </View>
            )
        return (<View></View>)
    }

    setFootHeight = (footHeight) => {
        this.setState({footHeight})
    }

    setpageIndex = (pageIndex)=>{
        this.state.pageIndex = pageIndex
        this.setState({pageIndex})
        this.getQuestionShowList()
    }

    ListFooterComponent = () => {
        return (<View style={{
            height: this.state.footHeight,
            width: Dimensions.get('window').width
        }}></View>)
    }

    renderItem = ({item, index}) => {
        if (item.question !== undefined) {
            return (
                <View style={styles.container}>
                    <TouchableHighlight
                        key={index}
                        onPress={() => {
                            this.showDetail(item)
                        }}
                        underlayColor={"#e6e6e6"}
                        style={[styles.touchableHighlight, item.originQuestion !== undefined && item.originQuestion.status === 'ac' ? styles.touchableHighlightAC : null]}>
                        <View style={styles.item}>
                            <View style={styles.item_text}>
                                <Text
                                    style={styles.title}>  {item.question.questionId}. {item.title} {(item.originQuestion.stat.acRate) ? item.originQuestion.stat.acRate : ((item.originQuestion.stat.total_acs / item.originQuestion.stat.total_submitted) * 100).toFixed(1) + "%"}</Text>
                                <Text style={styles.level}>{
                                    function () {
                                        if (item.originQuestion !== undefined)
                                            return (['--', '简单', '中等', '困难'][parseInt(item.originQuestion.difficulty.level)])
                                        else
                                            return '--'
                                    }()
                                }</Text>
                                {(item.originQuestion !== undefined && item.originQuestion.status === 'ac') ? (
                                    <Image style={styles.icon} source={require('../image/ok.png')}></Image>
                                ) : (
                                    <Text>未完成</Text>
                                )}
                            </View>
                        </View>
                    </TouchableHighlight>
                    {(index === this.state.questionShowList.length - 1
                        && this.state.questionShowList.length !== 0) ? (
                        <View style={styles.pageButtonContainer}>
                            <View style={styles.pageButton}>
                                <Button onPress={this.prePage} title={'上一页'}></Button>
                            </View>
                            <View style={styles.pageButton}>
                                <Button onPress={this.nextPage} title={'下一页'}></Button>
                            </View>
                        </View>
                    ) : (
                        <View></View>
                    )}
                </View>
            )
        } else {
            return (
                <Text>{JSON.stringify(item)}</Text>
            )
        }
    }

    render() {
        return (
            <View style={styles.container}>
                <FlatList
                    showsHorizontalScrollIndicator={false}
                    showsVerticalScrollIndicator={false}
                    data={this.state.questionShowList}
                    renderItem={this.renderItem}
                    onEndReached={this.onEndReached.bind(this)}
                    onEndReachedThreshold={0.5}
                    ListEmptyComponent={this.ListEmptyComponent}
                    ListHeaderComponent={this.ListHeaderComponent}
                    ListFooterComponent={this.ListFooterComponent}
                >
                </FlatList>
            </View>
        )
    }
}

const styles = StyleSheet.create({
    touchableHighlight: {
        height: 40,
        width: Dimensions.get('window').width - 15,
        backgroundColor: '#f1f1f1',
        borderWidth: 2,
        borderColor: '#cccccc',
        borderRadius: 5,
        marginTop: 8
    },
    container: {
        alignItems: 'center',
    },
    item: {
        justifyContent: 'center',
        height: 40,
    },

    item_text: {
        alignItems: 'center',
        height: 40,
        width: '100%',
        flexDirection: 'row',
    },
    title: {
        width: Dimensions.get('window').width - 30 - 100,
    },

    icon: {
        width: 25,
        height: 25,
    },
    level: {
        width: 50,
    },
    pageButtonContainer: {
        marginTop: 10,
        marginBottom: 10,
        flexDirection: 'row',
        width: Dimensions.get('window').width - 15,
        justifyContent: "space-around",
        alignItems: 'center'
    },
    pageButton: {
        width: (Dimensions.get('window').width - 15) / 2 - 30
    },
    touchableHighlightAC: {
        borderColor: '#4DBA4D',
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
    },
    listHeader: {
        marginTop: 8,
        flexDirection: 'row',
        width: Dimensions.get('window').width - 30,
        alignItems: 'center',
        justifyContent: 'space-around'
    },
    listHeaderItem: {
        width: Dimensions.get('window').width / 3 - 15
    },
    listHeaderText: {
        textAlign: 'center',
        fontSize: 15
    },
    sortIcon: {
        width: 20,
        height: 20,
        marginLeft: 6
    }
})
